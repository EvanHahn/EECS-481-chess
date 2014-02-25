// jshint camelcase: false
// jshint undef: false

function makeBoardFillScreen() {
	var boardWidth = Math.min($(window).width(), $(window).height() - $('#bottom-bar').height() - 20);
	$('#board').css('width', boardWidth);
	$('#bottom-bar .container').css('width', boardWidth);
}
makeBoardFillScreen();

var game = new Chess(ferry.getBoardState());
var board = new ChessBoard('board', {
	position: ferry.getBoardState(),
	showNotation: false,
	pieceTheme: 'vendor/chesspieces/{piece}.png',
	onChange: function() {
		saveGame();
		updateStatus();
		highlightLegalSquares();
	}
});

var $squares = $('#board div[class^="square-"]');

function removeLegalMoves() {
	removeAllScanning();
}

function showLegalMovesFor(square) {
	var $el = $('#board .square-' + square);
	ask.enable($el);
}

function removeAllScanning() {
	ask.disable($squares);
}

function saveGame() {

	var activePlayer;
	if (game.game_over())
		activePlayer = 'gameover';
	else if (game.turn() === 'w')
		activePlayer = ferry.getPlayer1();
	else
		activePlayer = ferry.getPlayer2();

	var boardState = game.fen();

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
updateStatus();

function highlightLegalSquares() {
	if (ferry.isMyTurn()) {
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

highlightLegalSquares();

$squares.on('click', function() {

	if (!ferry.isMyTurn())
		return;

	var source = $(this).data('square');

	if (board.currentPiece) {

		game.move({
			from: board.currentPiece,
			to: source,
			promotion: 'q' // TODO add UI for this
		});
		board.position(game.fen());
		removeLegalMoves();
		delete board.currentPiece;
		highlightLegalSquares();

	} else {

		var moves = game.moves({
			square: source,
			verbose: true
		});
		if (moves.length === 0) {
			return;
		}
		removeAllScanning();
		ask.enable(this);
		for (var i = 0; i < moves.length; i ++) {
			showLegalMovesFor(moves[i].to);
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
	$('#restart').hide();
}

$('#flip').click(function() {
	board.flip();
});
