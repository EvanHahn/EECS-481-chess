/* jshint browser: true */
/* global jQuery, NodeList */

(function() {

	var userAgent = navigator.userAgent.toLowerCase();
	var isAndroid = userAgent.indexOf('android') !== -1;
	var CLICK_EVENT = isAndroid ? 'tap' : 'click';

	var CSS = [
		'.askweb-overlay {',
			'position: absolute;',
			'top: 0;',
			'left: 0;',
			'width: 100%;',
			'height: 100%;',
		'}',
		'.askweb-scannable {',
      'border: 4px solid #00f !important;',
		'}',
		'.askweb-scannable-on {',
      'border: 4px solid #0f0 !important;',
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

	function handleClick(event) {
		var target = scanningTargets[scanIndex];
		event.stopPropagation();
		$(target).trigger('click'); // TODO: remove jQuery dependency
		return target;
	}

	var scanningEnabled = false;
	function enableScanning() {

		if (scanningEnabled)
			return;
		scanningEnabled = true;

		var overlay = document.createElement('div');
		overlay.className += 'askweb-overlay';

		overlay.addEventListener(CLICK_EVENT, handleClick);

		var css = document.createElement('style');
		css.innerHTML = CSS;
		document.head.appendChild(css);

		document.body.appendChild(overlay);

	}

	function forEach(elements, fn) {
		if ((typeof jQuery !== 'undefined') && (elements instanceof jQuery)) {
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
		},

    disableAll: function() {
      while (scanningTargets.length) {
        var target = scanningTargets[0];
        ask.disable(target);
      }
    },

		click: function() {
			var fakeEvent = {
				stopPropagation: function() {}
			};
			return handleClick(fakeEvent);
		}

	};

	if (typeof module !== 'undefined')
		module.exports = ask;
	else
		this.ask = ask;

})();
