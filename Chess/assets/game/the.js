var game = new Chess();

var boardWidth = Math.min($(window).width(), $(window).height());
$('#board').css('width', boardWidth);

var board = new ChessBoard('board', {
  draggable: true,
	position: 'start',
	showNotation: false,
	pieceTheme: 'vendor/chesspieces/{piece}.png',
	onDragStart: function(source, piece) {
		if (game.game_over() ||
		   (game.turn() === 'w' && piece.search(/^b/) !== -1) ||
		   (game.turn() === 'b' && piece.search(/^w/) !== -1)) {
			return false;
		}
	},
	onDrop: function(source, target) {
		var move = game.move({
			from: source,
			to: target,
			promotion: 'q' // NOTE: always promote to a queen
		});
		if (move === null) return 'snapback';
	},
	onSnapEnd: function() {
		board.position(game.fen());
	}
});
