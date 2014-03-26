// jshint camelcase: false
// jshint undef: false
// jshint quotmark: false

var game;
var board;

var $squares;
function updateSquares() {
  $squares = $('#board div[class^="square-"]');
}
updateSquares();

var isMyTurn = ferry.isMyTurn();

/*var whiteScanning = (game.turn() === 'w' && ferry.whiteScanning());
var blackScanning = (game.turn() === 'b' && ferry.blackScanning());
console.log(whiteScanning);
console.log(blackScanning);*/

function scanOver(square) {
  var $el = $('#board .square-' + square);
  ask.enable($el);
}

function saveGame() {
  var boardState = game.fen();
  var activePlayer;
  if (game.game_over())
    activePlayer = 'gameover';
  else if (game.turn() === 'w')
    activePlayer = ferry.getPlayer1();
  else
    activePlayer = ferry.getPlayer2();
  ferry.saveBoardState(activePlayer, boardState);
}

function updateStatus() {
  var status;
  var moveColor = 'White';
  if (game.turn() === 'b')
    moveColor = 'Black';
  if (game.in_checkmate()) {
    status = 'Game over! ' + moveColor + ' is in checkmate';
  } else if (game.in_draw()) {
    status = 'Game over! Draw';
  } else {
    status = moveColor + "'s turn";
    if (game.in_check())
      status += ' (in check!)';
  }
  $('#status').text(status);
}

function highlightLegalSquares() {
  if (isMyTurn) {
    $squares.each(function() {
      var source = $(this).data('square');
      if (game.moves({ square: source }).length) {
        ask.enable(this);
      } else {
        ask.disable(this);
      }
    });
  }
}

var $buttons = $('#buttons button');
function highlightButtons() {
  ask.enable($buttons);
}

$('#board').on('click', function(event) {

  if (!isMyTurn)
    return;

  var squareElement = event.target;
  var source = $(squareElement).data('square');

  if (board.currentPiece) {

    var moved = game.move({
      from: board.currentPiece,
      to: source,
      promotion: 'q' // TODO add UI for this
    });
    if (moved && !ferry.getIsPassAndPlay()) {
      isMyTurn = false;
    }

    board.position(game.fen());
    delete board.currentPiece;
    ask.disableAll();

    highlightLegalSquares();
    highlightButtons();

  } else {

    var moves = game.moves({
      square: source,
      verbose: true
    });
    if (moves.length === 0) {
      return;
    }
    ask.disableAll();
    ask.enable(squareElement);
    for (var i = 0; i < moves.length; i ++) {
      var moveSquare = moves[i].to;
      scanOver(moveSquare);
    }
    board.currentPiece = source;

  }

});

if (ferry.getIsPassAndPlay()) {
  $('#restart').click(function() {
    game.reset();
    board.position('start');
  });
} else {
  $('#restart').remove();
}

$('#flip').click(function() {
  board.flip();
  ask.disableAll();
  updateSquares();
  highlightLegalSquares();
  highlightButtons();
});

$('#back').click(function() {
  ferry.backButton();
});

$(document).ready(function() {

  function makeBoardFillScreen() {
    var boardWidth = Math.min($(window).width(), $(window).height() - $('#bottom-bar').height() - 20);
    $('#board').css('width', boardWidth);
    $('#bottom-bar .container').css('width', boardWidth);
  }
  makeBoardFillScreen();

  game = new Chess(ferry.getBoardState());
  board = new ChessBoard('board', {
    position: ferry.getBoardState(),
    showNotation: false,
    pieceTheme: 'vendor/chesspieces/{piece}.png',
    onChange: function() {
      saveGame();
      updateStatus();
      highlightLegalSquares();
      highlightButtons();
    }
  });

  updateSquares();

  highlightLegalSquares();
  highlightButtons();
  updateStatus();

});
