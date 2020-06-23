(function () {
    'use strict';
    angular
    .module('app')
    .controller('LoginController', Controller)
    .controller('LogoutController', LogoutController);

    function Controller($location, AuthenticationService, $scope, $http) {
        $scope.login = login;
        $scope.signUp = signUp;
        $scope.changeUserName = changeUserName;
        $scope.changePassword = changePassword;

        function changeUserName() {
            $scope.username = $scope.user.username;
        }

        function changePassword() {
            $scope.password = $scope.user.password;
        }

        function login() {
            AuthenticationService.Login($scope.username, $scope.password, function (result) {
                if (result === true) {
                    $location.path('/');
                }
            });
        };

        function signUp() {
            $http({
                url: "/sign-up",
                method: "POST",
                data: $scope.user
            })
            .then(function(response) {
                if (response.data.code == 200) {
                    $("#modalCreateUser").modal("hide");
                    toastr.success(response.data.message);
                } else {
                    toastr.error(response.data.message);
                }
            },function(response) {
                toastr.error(response.data.message);
            });
        }
    }

    function LogoutController(AuthenticationService) {
        AuthenticationService.Logout();
    }
})();