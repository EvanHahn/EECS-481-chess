function makeBoardFillScreen() {
	var boardWidth = Math.min($(window).width(), $(window).height() - $('#bottom-bar').height());
	$('#board').css('width', boardWidth);
}
makeBoardFillScreen();

var game = new Chess();
var board = new ChessBoard('board', {
	position: ferry.getBoardState(),
	showNotation: false,
	pieceTheme: 'vendor/chesspieces/{piece}.png',
	onChange: function() {
		updateStatus();
		highlightLegalSquares();
	}
});

var $squares = $('#board div[class^="square-"]');

function removeLegalMoves() {
	$squares.removeClass('legal-move');
}

function showLegalMovesFor(square) {
	var $el = $('#board .square-' + square);
	$el.addClass('legal-move');
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
	if (board.currentPiece) {
		game.move({
			from: board.currentPiece,
			to: source,
			promotion: 'q' // TODO add UI for this
		});
		board.position(game.fen());
		removeLegalMoves();
		delete board.currentPiece;
	} else {
		var moves = game.moves({
			square: source,
			verbose: true
		});
		if (moves.length === 0) {
			return false;
		}
		for (var i = 0; i < moves.length; i ++) {
			showLegalMovesFor(moves[i].to);
		}
		board.currentPiece = source;
	}
});

$('#restart').click(function() {
	game.reset();
	board.position('start');
});

$('#flip').click(function() {
	board.flip();
});
