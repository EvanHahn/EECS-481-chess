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
			return 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1';
		},
		isMyTurn: function() {
			return true;
		},
		backButton: function() {},
		saveBoardState: function() {},
		whiteScanning: function() {
			return true;
		},
		blackScanning: function() {
			return false;
		},
	};

}
