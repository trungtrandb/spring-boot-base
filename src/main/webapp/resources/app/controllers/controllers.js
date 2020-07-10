(function () {
    'use strict';
    var app = angular.module('app');
    app.controller("OrganizationController", OrganizationController);
    app.controller("GroupClassController", GroupClassController);
    app.controller("ClassController", ClassController);
    app.controller("StudentController", StudentController);
    app.controller("LessionController", LessionController);
    app.controller("EditStudentController", EditStudentController);
    app.controller("CheckinController", CheckinController);
    app.controller("ViewCheckinController", ViewCheckinController);
    app.controller("UserController", UserController);
    app.controller("DashboardController", DashboardController);
    app.controller("SubjectController", SubjectController);
    app.controller("TeacherController", TeacherController);
    app.controller("ParentController", ParentController);
    app.controller("ChatController", ChatController);

    /* ============================================ */
    function OrganizationController($scope, $http, Restangular) {
        $scope.submitOrganization = submitOrganization;
        $scope.remove = remove;
        $scope.edit = edit;
        loadLstOrganization();

        function submitOrganization() {
            $http({
                url: "/api/organization/insert",
                method: "POST",
                data: $scope.organization
            })
            .then(function(response) {
                toastr.success(response.data.message);
                loadLstOrganization();
                $("#modalAddOrganization").modal("hide");
                $scope.organization = {};
            },function (response) {
                toastr.error(response.data.message);
            });
        }

        function edit(organizationId) {
            Restangular.one('/api/organization/get', organizationId).get().then(function (response) {
                $scope.organization = response.data;
                $("#modalAddOrganization").modal("show");
            });
        }

        function loadLstOrganization() {
            $http.get("/api/organization/get-by-user").then(function (response) {$scope.lstOrganization = response.data.data;});
        }

        function remove(id){
            Restangular.one('/api/organization/delete', id).get().then(function (response) {
                if (response.code == 200) {
                    toastr.success(response.message);
                    loadLstOrganization();
                }else{
                    toastr.error(response.message);
                }
            });
        }
    }


    /* ============================================ */
    function GroupClassController($scope, $http, Restangular) {
        $scope.submitAddGroup = submitAddGroupClass;
        $scope.remove = remove;
        $scope.edit = edit;
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
                $scope.group = {};
            },function (response) {
                toastr.error(response.data.message);
            });
        }

        function edit(id) {
            Restangular.one('/api/group-class/get', id).get().then(function (response) {
                if (response.code == 200) {
                    $("#modalAddGroup").modal("show");
                    $scope.group = response.data;
                    $scope.group.organizationId = response.data.organization.id;
                }else{
                    toastr.error(response.message);
                }
            });
        }

        function remove(id){
            Restangular.one('/api/group-class/delete', id).get().then(function (response) {
                if (response.code == 200) {
                    toastr.success(response.message);
                    loadLstGroup();
                }else{
                    toastr.error(response.message);
                }
            });
        }
    }


    /* ============================================ */
    function ClassController($scope, $http, Restangular) {
        $scope.submitAddCLazz = submitAddClazz;
        $scope.remove = remove;
        $scope.selectOrganization = selectOrganization;
        loadLstClass();
        Restangular.one("/api/organization/get-by-user").get().then(function (response) { $scope.lstOrganization = response.data; });

        function selectOrganization() {
            Restangular.one("/api/group-class/get-by-organization?id=" + $scope.organizationId).get().then(function (response) { $scope.lstGroup = response.data; });
            Restangular.one("/api/organization/get-teacher?id=" + $scope.organizationId).get().then(function (response) { $scope.lstTeacher = response.data; });
        }
        
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
            $http.get("/api/class/delete/" + id).then(function (response) {
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
    function LessionController($scope, $http, Restangular) {
        $scope.lession = {};
        $scope.submitAddLession = submitAddLession;
        $scope.remove = remove;
        $scope.selectOrganization = selectOrganization;

        flatpickr(".datetimepicker",{
            enableTime: true,
            dateFormat: "Y-m-d H:i",
        });
        loadLstLession();
        Restangular.one("/api/organization/get-by-user").get().then(function (response) { $scope.lstOrganization = response.data; });
        Restangular.one("/api/class/get-by-group").get().then(function (response) { $scope.lstClass = response.data; });

        function selectOrganization() {
            Restangular.one("/api/organization/get-teacher?id=" + $scope.organizationId).get().then(function (response) { $scope.lstTeacher = response.data; });
            Restangular.one("/api/subject/get-by-org?id=" + $scope.organizationId).get().then(function (response) { $scope.lstSubject = response.data; });            
        }

        function loadLstLession() {
            $http.get("/api/lession/getAll").then(function (response) {$scope.lstLession = response.data.data;});
        }


        function remove(id){
            $http.get("/api/lession/delete/" + id).then(function (response) {
                if(response.data.code == 200){
                    toastr.success(response.data.message);
                    loadLstLession();
                }else{
                    toastr.error(response.data.message);
                }
            }, function (response) {
                toastr.error(response.data.message);
            });
        }


        function submitAddLession() {
            $http({
                url: "/api/lession/insert",
                method: "POST",
                data: $scope.lession
            })
            .then(function(response) {
                if(response.data.code == 200){
                    toastr.success(response.data.message);
                    loadLstLession();
                    $("#modalAddLession").modal("hide");
                }else{
                    toastr.error(response.data.message);
                }
                
            },function (response) {
                toastr.error(response.data.message);
            });
        }
    }

    /* ============================================ */
    function CheckinController($scope, $http, $location, $filter, Restangular) {
        $scope.submitCheckin = submitCheckin;
        $scope.selectClass = selectClass;
        $scope.view = view;
        $scope.checkin = $scope.filter = {};
        $scope.filterCheckin = filterCheckin;
        
        filterCheckin();
        flatpickr(".datetimepicker",{
            dateFormat: "Y-m-d",
        });

        Restangular.one("/api/class/get-by-group").get().then(function (response) { $scope.lstClass = response.data; });
        Restangular.one('/api/organization/get-by-user').get().then(function (response) { $scope.lstOrganization = response.data; });
        Restangular.one('/api/lession/getAll').get().then(function (response) { $scope.lstLession = response.data; });

        function selectClass() {
            Restangular.one("/api/student/getAll?class=" +$scope.checkin.classId).get().then(function (response) { $scope.lstStudent = response.data; });
            Restangular.one('/api/lession/get-by-class?id='+$scope.checkin.classId).get().then(function (response) { $scope.lstLession = response.data; });
        }

        function view(checkinItem) {
            $location.path("/view-checkin").search({
                'classId': checkinItem.classId, 
                'date': $filter('date')(new Date(checkinItem.createdDate),'yyyy-MM-dd'),
                'subjectId' :checkinItem.subjectId
            });
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

    /* ============================================ */
    function ViewCheckinController($scope,$location, Restangular) {
        var paramSeach = $location.search();
        Restangular.all("/api/checkin/get").getList($location.search()).then(function(response) {
            console.log(response);            
        })   
    }


    /* ============================================ */
    function UserController($scope, $http, Restangular, $rootScope) {
        $scope.submitForm = submitForm;
        $scope.user = {};
        Restangular.one('/api/user/getInfo').get().then(function (response) { $scope.user = response.data; });

        function submitForm() {
            Restangular.all('/api/user/update').post($scope.user).then(function (response) {
                if(response.code == 200){
                    toastr.success(response.message);
                    $rootScope.currentUser.avatar = response.data.avatar;
                    $rootScope.currentUser.fullName = response.data.fullName;
                }else{
                    toastr.error(response.message);
                }
            });
        }

        $scope.uploadFile = function(files) {
            var data = new FormData();
            data.append("file", files[0]);
            $http.post("/upload-image", data, {
                withCredentials: true,
                headers: {'Content-Type': undefined },
                transformRequest: angular.identity
            }).then(function (response) {
                $scope.user.avatar = response.data.data;
            }, function (response) {
                toastr.error(response.data.message);
            })
        };
    }

    /* ============================================ */
    function DashboardController($scope, $rootScope, Restangular) {
        // body...
    }

    /* ============================================ */
    function ChatController($scope, userName, Restangular, $rootScope, $filter) {
        $scope.sendMessage = sendMessage;
        $scope.pressSend = pressSend;

        var socket = new SockJS('/ws');
        var stompClient = Stomp.over(socket);
        document.cookie = 'Authorization=' + $rootScope.currentUser.token + '; path=/';  
        stompClient.reconnect_delay = 3000;
        stompClient.connect({}, function(frame) {
            // stompClient.subscribe('/topic/message', function(messageOutput) {
            //     var resMessage = JSON.parse(messageOutput.body);
            //     if (resMessage.from == $rootScope.currentUser.userName) {
            //         var selfMess = "";
            //         var floatName = "float-left";
            //         var floatTime = "float-right";
            //     }else{
            //         var selfMess = "right";
            //         var floatName = "float-right";
            //         var floatTime = "float-left";
            //     }
            //     var html = '<div class="direct-chat-msg '+ selfMess + '">';
            //     html += '<div class="direct-chat-infos clearfix">';
            //     html += '<span class="direct-chat-name '+ floatName +'">' + resMessage.fullName +'</span>';
            //     html += '<span class="direct-chat-timestamp '+ floatTime +'">'+ resMessage.time +'</span></div>';
            //     html += '<img class="direct-chat-img" src="'+ resMessage.avatar +'">';
            //     html += '<div class="direct-chat-text">'+resMessage.text+'</div>';
            //     $("#box-chat").append(html);
            // });
            stompClient.subscribe("/user/queue/reply", function(messageOutput) {
                var resMessage = JSON.parse(messageOutput.body);

                var time = $filter('date')(new Date(resMessage.time),'yyyy-MM-dd HH:MM:ss');
                var html = '<div class="direct-chat-msg right">';
                html += '<div class="direct-chat-infos clearfix">';
                html += '<span class="direct-chat-name float-right">' + resMessage.fullName +'</span>';
                html += '<span class="direct-chat-timestamp float-left">'+ time +'</span></div>';
                html += '<img class="direct-chat-img" src="'+ resMessage.avatar +'">';
                html += '<div class="direct-chat-text">'+resMessage.text+'</div>';
                $("#box-chat").append(html);
            })
        });

        function pressSend(event) {
            if(event.keyCode == 13) {
                sendMessage();
            }
        }

        function sendMessage() {

            var time = $filter('date')(Date.now(),'yyyy-MM-dd HH:MM:ss');
            var html = '<div class="direct-chat-msg">';
                html += '<div class="direct-chat-infos clearfix">';
                html += '<span class="direct-chat-name float-left">' + $rootScope.currentUser.fullName +'</span>';
                html += '<span class="direct-chat-timestamp float-right">'+ time +'</span></div>';
                html += '<img class="direct-chat-img" src="'+ $rootScope.currentUser.avatar +'">';
                html += '<div class="direct-chat-text">'+ $scope.messageContent +'</div>';
                $("#box-chat").append(html);
            // stompClient.send("/app/topic/chat", {}, JSON.stringify({'text': $scope.messageContent}));
            stompClient.send("/app/direct/chat", {}, JSON.stringify({'text': $scope.messageContent, 'to': userName}));
            $scope.messageContent = "";
            return;
        }
    }

    function SubjectController($scope, Restangular) {
        $scope.submitSubject = submitSubject;
        Restangular.one("/api/organization/get-by-user").get().then(function (response) { $scope.lstOrganization = response.data;});
        loadLstSubject();

        function loadLstSubject() {
            Restangular.one("/api/subject/getAll").get().then(function (response) {$scope.lstSubject = response.data;});
        }
        function submitSubject() {
            Restangular.all('/api/subject/insert').post($scope.subject).then(function (response) {
                if(response.code == 200){
                    loadLstSubject();
                    $("#modalAddSubject").modal("hide");
                    toastr.success(response.message);
                    $scope.subject = {};
                }else{
                    toastr.error(response.message);
                }
            }, function(response) {
                toastr.error(response.data.message);
            });
        }
    }

    function TeacherController($scope, $http, Restangular) {
        $scope.submitUser = submitUser;
        $scope.remove = remove;

        loadLstTeacher();
        Restangular.one("/api/organization/get-by-user").get().then(function (response) { $scope.lstOrganization = response.data;});

        function loadLstTeacher() {
            Restangular.one("/api/organization/get-teacher").get().then(function (response) {$scope.lstTeacher = response.data;});
        }

        function submitUser() {
            Restangular.all('/sign-up').post($scope.user).then(function (response) {
                if(response.code == 200){
                    loadLstTeacher();
                    $("#modalCreateUser").modal("hide");
                    toastr.success(response.message);
                    $scope.subject = {};
                }else{
                    toastr.error(response.message);
                }
            }, function(response) {
                toastr.error(response.data.message);
            });
        }

        function remove(teacherId, orgId){
            $http.get("/api/organization/delete-teacher?teacher_id="+ teacherId + "&org_id=" + orgId).then(function (response) {
                if(response.data.code == 200){
                    toastr.success(response.data.message);
                    loadLstTeacher();
                }else{
                    toastr.error(response.data.message);
                }
            }, function (response) {
                toastr.error(response.data.message);
            });
        }
    }

    function ParentController($scope, Restangular) {
        $scope.orgId = "";
        Restangular.one("/api/organization/get-parent?id=" + $scope.orgId).get().then(function (response) { $scope.lstParent = response.data; });
    }
})();