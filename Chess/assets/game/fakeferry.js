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
			return 'start';
		},
		isMyTurn: function() {
			return true;
		},
		saveBoardState: function() {}
	};

}
