(function() {
  "use strict";
  angular
  .module("app")
  .factory("AuthenticationService", Service);

  function Service($http, $localStorage, $rootScope) {
    var service = {};
    service.Login = Login;
    service.Logout = Logout;

    return service;

    function Login(username, password, callback) {
      return $http({
        url: "/authenticate",
        method: "POST",
        data: { username: username, password: password }
      })
      .then(function(response) {
        if (response.data.token) {
          var token = response.data.token;
          var payloadToken = jwt_decode(token);
          var user = {exp: payloadToken.exp, token: token, fullName: payloadToken.fullName}

          $localStorage.currentUser = $rootScope.currentUser = user;
          $http.defaults.headers.common.Authorization = "Bearer " + token;
          callback(true);
        } else {
          callback(false);
        }
      }, function(response) {
        toastr.error(response.data.message);
      });
    }

    function Logout() {
      delete $localStorage.currentUser;
      $http.defaults.headers.common.Authorization = "";
      setTimeout(function(){ location.reload(); }, 1000);
    }
  }
})();