angular.module("app").service("ChatService", function($q, $timeout, $rootScope) {

  var service = {}, listener = $q.defer(), socket = {
    client: null,
    stomp: null
  };

  service.RECONNECT_TIMEOUT = 5000;
  service.SOCKET_URL = "/ws";
  service.CHAT_BROKER = "/app/chat";
  service.MESSAGE_TOPIC = "/topic/message";

  service.CHAT_DIRECT = "/app/direct/chat";
  service.MESSAGE_DIRECT = "/user/queue/reply";

  service.receive = function() {
    return listener.promise;
  };

  service.sendDirect = function(message) {
    socket.stomp.send(service.CHAT_DIRECT, {}, message);
  };

  var reconnect = function() {
    $timeout(function() {
      initialize();
    }, this.RECONNECT_TIMEOUT);
  };

  var getMessage = function(data) {
    var message = JSON.parse(data), out = {};
    return out;
  };

  var startListener = function() {
    socket.stomp.subscribe(service.MESSAGE_TOPIC, function(data) {
      listener.notify(getMessage(data.body));
    });

    socket.stomp.subscribe(service.MESSAGE_DIRECT, function(data) {
      listener.notify(JSON.parse(data.body));
    });
  };

  var initialize = function() {
    document.cookie = 'Authorization=' + $rootScope.currentUser.token + '; path=/';  
    socket.client = new SockJS(service.SOCKET_URL);
    socket.stomp = Stomp.over(socket.client);
    socket.stomp.connect({}, startListener);
    socket.stomp.onclose = reconnect;
  };

  initialize();
  return service;
});