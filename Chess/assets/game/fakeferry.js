if (typeof ferry === 'undefined') {

	ferry = {
		getCurrentUser: function() {
			return 'USER1';
		},
		getPlayer1: function() {
			return 'USER1';
		},
		getPlayer2: function() {
			return 'USER2';
		},
		getIsPassAndPlay: function() {
			return true;
		},
		getBoardState: function() {
			return 'rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2';
		}
	};

}
