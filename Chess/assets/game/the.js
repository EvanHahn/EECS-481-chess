var game = new Chess();

var boardWidth = Math.min($(window).width(), $(window).height());
$('#board').css('width', boardWidth);

var board = new ChessBoard('board', {
	position: 'start',
	showNotation: false,
	pieceTheme: 'vendor/chesspieces/{piece}.png',
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

$('#board div[class^="square-"]').on('click', function() {

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
