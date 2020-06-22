(function () {
    'use strict';
    var app = angular.module('app');
    app.controller("OrganizationController", OrganizationController);
    app.controller("GroupClassController", GroupClassController);
    app.controller("ClassController", ClassController);
    app.controller("StudentController", StudentController);
    app.controller("SubjectController", SubjectController);
    app.controller("EditStudentController", EditStudentController);
    app.controller("CheckinController", CheckinController);

    /* ============================================ */
    function OrganizationController($scope, $http) {
        $scope.submitAddOrganization = submitAddOrganization;
        loadLstOrganization();
        $scope.remove = remove;

        function submitAddOrganization() {
            $http({
                url: "/api/organization/insert",
                method: "POST",
                data: $scope.organization
            })
            .then(function(response) {
                toastr.success(response.data.message);
                loadLstOrganization();
                $("#modalAddOrganization").modal("hide");
            },function (response) {
                toastr.error(response.data.message);
            });
        }

        function loadLstOrganization() {
            $http.get("/api/organization/get-by-user").then(function (response) {$scope.lstOrganization = response.data.data;});
        }

        function remove(id){
            $http.get("/api/organization/delete/" + id).then(function (response) {
                toastr.success(response.data.message);
                loadLstOrganization();
            }, function (response) {
                toastr.error(response.data.message);
            });
        }
    }


    /* ============================================ */
    function GroupClassController($scope, $http) {
        $scope.submitAddGroup = submitAddGroupClass;
        $scope.remove = remove;
        loadLstGroup();
        $http.get("/api/organization/get-by-user").then(function (response) {$scope.lstOrganization = response.data.data;});

        function loadLstGroup() {
            $http.get("/api/group-class/get-by-organization").then(function (response) {$scope.lstGroupClass = response.data.data;});
        }

        function submitAddGroupClass() {
            $http({
                url: "/api/group-class/insert",
                method: "POST",
                data: $scope.group
            })
            .then(function(response) {
                toastr.success(response.data.message);
                loadLstGroup();
                $("#modalAddGroup").modal("hide");
            },function (response) {
                toastr.error(response.data.message);
            });
        }

        function remove(id){
            $http.get("/api/group-class/delete/" + id).then(function (response) {
                if(response.data.code == 200){
                    toastr.success(response.data.message);
                    loadLstGroup();
                }else{
                    toastr.error(response.data.message);
                }
            }, function (response) {
                toastr.error(response.data.message);
            });
        }
    }


    /* ============================================ */
    function ClassController($scope, $http) {
        $scope.submitAddCLazz = submitAddClazz;
        $scope.remove = remove;
        loadLstClass();
        $http.get("/api/group-class/get-by-organization").then(function (response) {$scope.lstGroup = response.data.data;});

        function loadLstClass() {
            $http.get("/api/class/get-by-group").then(function (response) {$scope.lstClass = response.data.data;});
        }

        function submitAddClazz() {
            $http({
                url: "/api/class/insert",
                method: "POST",
                data: $scope.class
            })
            .then(function(response) {
                toastr.success(response.data.message);
                loadLstClass();
                $("#modalAddClass").modal("hide");
            },function (response) {
                toastr.error(response.data.message);
            });
        }

        function remove(id){
            $http.get("/api/group-class/delete/" + id).then(function (response) {
                if(response.data.code == 200){
                    toastr.success(response.data.message);
                    loadLstClass();
                }else{
                    toastr.error(response.data.message);
                }
            }, function (response) {
                toastr.error(response.data.message);
            });
        }
    }


    /* ============================================ */
    function StudentController($scope, $http, $location) {
        $scope.submitStudent = submitAddStudent;
        $scope.remove = remove;
        loadLstStudent();
        $http.get("/api/class/get-by-group").then(function (response) {$scope.lstClass = response.data.data;});

        function loadLstStudent() {
            $http.get("/api/student/getAll").then(function (response) {$scope.lstStudent = response.data.data;});
        }


        function remove(student){

            var id = student.id;
            var classId = student.classes[0];
            $http.get("/api/student/delete?student=" + id+"&&class="+classId).then(function (response) {
                if(response.data.code == 200){
                    toastr.success(response.data.message);
                    loadLstStudent();
                }else{
                    toastr.error(response.data.message);
                }
            }, function (response) {
                toastr.error(response.data.message);
            });
        }


        function submitAddStudent() {
            $http({
                url: "/api/student/insert",
                method: "POST",
                data: $scope.student
            })
            .then(function(response) {
                if(response.data.code == 200){
                    toastr.success(response.data.message);
                    $location.path("/student");
                }else{
                    toastr.error(response.data.message);
                }
                
            },function (response) {
                toastr.error(response.data.message);
            });
        }
    }


    /* ============================================ */
    function EditStudentController($scope, $http, $location, studentId) {
        $scope.submitStudent = submitStudent;
        $http.get("/api/class/get-by-group").then(function (response) {$scope.lstClass = response.data.data;});
        $http.get("/api/student/get/" + studentId).then(function (response) {
            $scope.student = response.data.data;
            $scope.student.dateOfBirth = new Date(response.data.data.dateOfBirth);
        });

        function submitStudent() {
            $http({
                url: "/api/student/update",
                method: "PUT",
                data: $scope.student
            })
            .then(function(response) {
                if(response.data.code == 200){
                    toastr.success(response.data.message);
                    $location.path("/student");
                }else{
                    toastr.error(response.data.message);
                }
            },function (response) {
                toastr.error(response.data.message);
            });
        }
    }


    /* ============================================ */
    function SubjectController($scope, $http) {
        flatpickr(".datetimepicker",{
            enableTime: true,
            dateFormat: "Y-m-d H:i",
        });
        $scope.subject = {};
        $scope.submitAddSubject = submitAddSubject;
        $scope.remove = remove;
        loadLstSubject();
        $http.get("/api/class/get-by-group").then(function (response) {$scope.lstClass = response.data.data;});

        function loadLstSubject() {
            $http.get("/api/subject/getAll").then(function (response) {$scope.lstSubject = response.data.data;});
        }


        function remove(id){
            $http.get("/api/subject/delete/" + id).then(function (response) {
                if(response.data.code == 200){
                    toastr.success(response.data.message);
                    loadLstSubject();
                }else{
                    toastr.error(response.data.message);
                }
            }, function (response) {
                toastr.error(response.data.message);
            });
        }


        function submitAddSubject() {
            $http({
                url: "/api/subject/insert",
                method: "POST",
                data: $scope.subject
            })
            .then(function(response) {
                if(response.data.code == 200){
                    toastr.success(response.data.message);
                    loadLstSubject();
                    $("#modalAddSubject").modal("hide");
                }else{
                    toastr.error(response.data.message);
                }
                
            },function (response) {
                toastr.error(response.data.message);
            });
        }
    }

    /* ============================================ */
    function CheckinController($scope, $http, Restangular) {
        $scope.submitCheckin = submitCheckin;
        $scope.selectClass = selectClass;
        $scope.checkin = $scope.filter = {};
        $scope.filterCheckin = filterCheckin;
        
        filterCheckin();
        flatpickr(".datetimepicker",{
            dateFormat: "Y-m-d",
        });

        Restangular.one("/api/class/get-by-group").get().then(function (response) { $scope.lstClass = response.data; });
        Restangular.one('/api/subject/getAll').get().then(function (response) { $scope.lstSubject = response.data; });
        Restangular.one('/api/organization/get-by-user').get().then(function (response) { $scope.lstOrganization = response.data; });

        function selectClass() {
            $http.get("/api/student/getAll?class=" +$scope.checkin.classId).then(function (response) {$scope.lstStudent = response.data.data;});
        }

        function filterCheckin() {
            Restangular.all('/api/checkin/getAll').post($scope.filter).then(function (response) {
                if(response.code == 200){
                    $scope.lstCheckinDTO = response.data;
                }else{
                    toastr.error(response.message);
                }
            });
        }

        function submitCheckin(studentId, status) {  
            $scope.checkin.studentId = studentId;
            $scope.checkin.status = status;  
            Restangular.all('/api/checkin/insert').post($scope.checkin).then(function (response) {
                if(response.code == 200){
                    toastr.success(response.message);
                }else{
                    toastr.error(response.message);
                }
            });
        }
    }
})();