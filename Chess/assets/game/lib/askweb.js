(function() {

	var CSS = [
		'.askweb-overlay {',
			'position: fixed;',
			'top: 0;',
			'left: 0;',
			'width: 100%;',
			'height: 100%;',
		'}',
		'.askweb-scannable {',
			'box-shadow: inset 0 0 0 3px #00f !important;',
		'}',
		'.askweb-scannable-on {',
			'box-shadow: inset 0 0 0 3px #0f0 !important;',
		'}'
	].join('\n');

	var scanningSpeed = 1000;

	var scanningTargets = [];
	var scanIndex = 0;

	var scanTimeout;

	function scan() {

		if (scanningTargets.length === 0)
			return;

		scanningTargets.forEach(function(target) {
			target.classList.remove('askweb-scannable-on');
		});

		scanningTargets[scanIndex].classList.add('askweb-scannable-on');

		scanTimeout = setTimeout(function() {
			scanIndex = (scanIndex + 1) % scanningTargets.length;
			scan();
		}, scanningSpeed);

	}

	function restartScanning() {
		clearTimeout(scanTimeout);
		scanIndex = 0;
		scan();
	}

	var scanningEnabled = false;
	function enableScanning() {

		if (scanningEnabled)
			return;
		scanningEnabled = true;

		var overlay = document.createElement('div');
		overlay.className += 'askweb-overlay';

		overlay.addEventListener('click', function(event) {
			event.stopPropagation();
			scanningTargets[scanIndex].click();
		});

		var css = document.createElement('style');
		css.innerHTML = CSS;
		document.head.appendChild(css);

		document.body.appendChild(overlay);

	}

	function forEach(elements, fn) {
		if (jQuery && (elements instanceof jQuery)) {
			elements.each(function() {
				fn(this);
			});
		} else if (elements instanceof NodeList) {
			for (var i = 0; i < elements.length; i ++) {
				fn(elements[i]);
			}
		} else {
			fn(elements);
		}
	}

	var ask = {

		enable: function(elements) {
			forEach(elements, function(element) {
				element.classList.add('askweb-scannable');
				scanningTargets.push(element);
				restartScanning();
			});
			if (!scanningEnabled)
				enableScanning();
		},

		disable: function(elements) {
			forEach(elements, function(element) {
				element.classList.remove('askweb-scannable');
				element.classList.remove('askweb-scannable-on');
				for (var i = 0; i < scanningTargets.length; i ++) {
					if (scanningTargets[i] === element) {
						scanningTargets.splice(i, 1);
						break;
					}
				}
				restartScanning();
			});
		}

	};

	if (typeof module !== 'undefined')
		module.exports = ask;
	else
		this.ask = ask;

})();
