var game = new Chess();

var boardWidth = Math.min($(window).width(), $(window).height());
$('#board').css('width', boardWidth);

var board = new ChessBoard('board', {
	position: ferry.getBoardState(),
	showNotation: false,
	pieceTheme: 'vendor/chesspieces/{piece}.png',
	onChange: function() {
		updateStatus();
		highlightLegalSquares();
	}
});

var removeGreySquares = function() {
  $('#board .square-55d63').css('background', '');
};

var greySquare = function(square) {
  var squareEl = $('#board .square-' + square);

  var background = '#a9a9a9';
  if (squareEl.hasClass('black-3c85d') === true) {
    background = '#696969';
  }

  squareEl.css('background', background);
};


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

updateStatus();

var $squares = $('#board div[class^="square-"]');

function highlightLegalSquares() {
	$squares.each(function() {
		var source = $(this).data('square');
		if (game.moves({ square: source }).length) {
			$(this).css('box-shadow', 'inset 0 0 10px red');
		} else {
			$(this).css('box-shadow', 'none');
		}
	});
}

highlightLegalSquares();

$squares.on('click', function() {

	var source = $(this).data('square');
	var piece = game.get(source);

	if (board.currentPiece) {

		var move = game.move({
			from: board.currentPiece,
			to: source,
			promotion: 'q' // TODO add UI for this
		});

		board.position(game.fen());

		removeGreySquares();
		delete board.currentPiece;

	} else {

		var moves = game.moves({
			square: source,
			verbose: true
		});

		if (game.game_over() ||
		  (!piece) ||
		  (game.turn() === 'w' && piece.color !== game.WHITE) ||
		  (game.turn() === 'b' && piece.color !== game.BLACK) ||
		  (moves.length === 0)) {
		  return false;
		}

		moves.forEach(function(move) {
			greySquare(move.to);
		});

		board.currentPiece = source;

	}

	return false;

});

$('#restart').click(function() {
	game.reset();
	board.position('start');
});

$('#flip').click(function() {
	board.flip();
});
