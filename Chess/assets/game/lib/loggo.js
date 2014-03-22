(function() {

  var METHODS = [
    { name: 'log', color: '#fff' },
    { name: 'info', color: '#99f' },
    { name: 'warn', color: '#ff0' },
    { name: 'error', color: '#f00' }
  ];

  var container = document.createElement('ul');
  container.style.position = 'absolute';
  container.style.top = container.style.left = '10px';
  container.style.maxWidth = '50%';
  document.body.appendChild(container);

  var loggo = {};

  METHODS.forEach(function(method) {

    loggo[method.name] = function() {

      var args = Array.prototype.slice.call(arguments, 0);
      var output = args.join(' ');

      var message = document.createElement('li');
      message.style.background = 'black';
      message.style.padding = '1em';
      message.style.marginBottom = '.5em';
      message.style.color = method.color;
      message.innerHTML = output;
      container.appendChild(message);

      setTimeout(function() {
        container.removeChild(message);
      }, 5000);

    };

  });

  if (typeof module !== 'undefined')
    module.exports = loggo;
  else
    this.loggo = loggo;

})();
