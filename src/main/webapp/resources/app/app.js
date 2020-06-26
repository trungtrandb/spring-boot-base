(function() {

  // const BASE_URL = "http://localhost:8000/";
  var app = angular
  .module("app", ["ngMessages", "ngStorage", "ngRoute", "restangular"])
  .config(config)
  .run(run);

  function config($routeProvider, RestangularProvider) {
    // RestangularProvider.setBaseUrl(BASE_URL);
    $routeProvider
    .when("/", {
      templateUrl : "/resources/app/views/dashboad.html",
      controller : "DashboardController"
    })
    .when("/create-student", {
      templateUrl : "/resources/app/views/create-student.html",
      controller : "StudentController"
    })
    .when("/organization", {
      templateUrl : "/resources/app/views/organization.html",
      controller : "OrganizationController"
    })
    .when("/checkin", {
      templateUrl : "/resources/app/views/checkin.html",
      controller : "CheckinController"
    })
    .when("/create-checkin", {
      templateUrl : "/resources/app/views/create-checkin.html",
      controller : "CheckinController"
    })
    .when("/view-checkin", {
      templateUrl : "/resources/app/views/view-checkin.html",
      controller : "ViewCheckinController"
    })
    .when("/subject", {
      templateUrl : "/resources/app/views/subject.html",
      controller : "SubjectController",
    })
    .when("/edit-student/:id", {
      templateUrl : "/resources/app/views/create-student.html",
      controller : "EditStudentController",
      resolve: {
          studentId: function ($route) {
              return $route.current.params.id;
          }
      }
    })
    .when("/widgets", {
      templateUrl : "/resources/app/views/widgets.html"
    })
    .when("/class", {
      templateUrl : "/resources/app/views/class.html",
      controller : "ClassController"
    })
    .when("/student", {
      templateUrl : "/resources/app/views/student.html",
      controller : "StudentController",
    })
    .when("/group-class", {
      templateUrl : "/resources/app/views/group-class.html",
      controller : "GroupClassController"
    })
    .when("/user-info", {
      templateUrl : "/resources/app/views/user-info.html",
      controller : "UserController"
    })
    .when("/login", {
      templateUrl : "/resources/app/login.html",
    })
    .when('/logout', {
      template: '', //A template or templateUrl is required by AngularJS, even if your controller always redirects.
      controller: 'LogoutController'
    });
  }

  function run($rootScope, $http, $location, $localStorage, AuthenticationService) {
    if ($localStorage.currentUser) {
      $rootScope.currentUser = $localStorage.currentUser;
      $http.defaults.headers.common.Authorization = "Bearer " + $localStorage.currentUser.token;
      if($localStorage.currentUser && $localStorage.currentUser.exp < (new Date().getTime() / 1000)) AuthenticationService.Logout();
    }

    $rootScope.$on("$locationChangeStart", function(event, next, current) {
      var publicPages = ["/login"];
      var restrictPage = publicPages.indexOf($location.path()) === -1;
      if(restrictPage && !$localStorage.currentUser) $location.path("login");
    });
  }
})();