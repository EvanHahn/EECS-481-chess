var boardWidth = Math.min($(window).width(), $(window).height());
$('#board').css('width', boardWidth);

var board = new ChessBoard('board', {
	position: 'start',
	showNotation: false,
	pieceTheme: 'vendor/chesspieces/{piece}.png'
});
