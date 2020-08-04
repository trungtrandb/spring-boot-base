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
    app.controller("NotifyController", NotifyController);
    app.controller("NavbarController", NavbarController);
    app.controller("PointController", PointController);


    /* ============================================ */
    function NavbarController($scope, ChatService, Restangular, $rootScope){
        $scope.markAllRead = markAllRead;
        Restangular.one("/api/notify/count-notify").get().then(function (response) { $scope.numNotiUnRead = response.data.notRead; });
        Restangular.one("/api/notify/get-by-user").get().then(function (response) { $scope.lstNotify = response.data; });
        Restangular.one("/api/organization/get-by-user").get().then(function (response) { 
            $rootScope.org = response.data; 
        });

        function markAllRead() {
            Restangular.all("/api/notify/is-read").post().then(function (response) { location.reload(); });
        }
    }

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
        flatpickr(".datetimepicker",{
            dateFormat: "Y",
        });
        loadLstGroup();

        function loadLstGroup() {
            $http.get("/api/group-class/get-all").then(function (response) {$scope.lstGroupClass = response.data.data;});
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
        loadLstClass();
        
        Restangular.one("/api/group-class/get-all").get().then(function (response) { $scope.lstGroup = response.data; });
        Restangular.one("/api/organization/get-teacher").get().then(function (response) { $scope.lstTeacher = response.data; });
        
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
                $scope.class = {};
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
        bsCustomFileInput.init();
        $scope.uploadFile = uploadFile;
        $scope.student = {};

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

        function uploadFile(files) {
            var data = new FormData();
            data.append("file", files[0]);
            $http.post("/upload-image", data, {
                withCredentials: true,
                headers: {'Content-Type': undefined },
                transformRequest: angular.identity
            }).then(function (response) {
                $scope.student.avatar = response.data.data;
            }, function (response) {
                toastr.error(response.data.message);
            })
        };
    }


    /* ============================================ */
    function EditStudentController($scope, $http, $location, studentId) {
        $scope.submitStudent = submitStudent;
        $scope.uploadFile = uploadFile;
        bsCustomFileInput.init();
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

        function uploadFile(files) {
            var data = new FormData();
            data.append("file", files[0]);
            $http.post("/upload-image", data, {
                withCredentials: true,
                headers: {'Content-Type': undefined },
                transformRequest: angular.identity
            }).then(function (response) {
                $scope.student.avatar = response.data.data;
            }, function (response) {
                toastr.error(response.data.message);
            })
        };
    }


    /* ============================================ */
    function LessionController($scope, $http, Restangular) {
        $scope.lession = {};
        $scope.submitAddLession = submitAddLession;
        $scope.remove = remove;

        flatpickr(".datetimepicker",{
            enableTime: true,
            dateFormat: "Y-m-d H:i",
        });
        loadLstLession();
        Restangular.one("/api/class/get-by-group").get().then(function (response) { $scope.lstClass = response.data; });
        Restangular.one("/api/organization/get-teacher").get().then(function (response) { $scope.lstTeacher = response.data; });
        Restangular.one("/api/subject/getAll").get().then(function (response) { $scope.lstSubject = response.data; });  

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
    function NotifyController($scope,$location, Restangular) {
        $scope.submitNotify = submitNotify;
        $scope.remove = remove;
        $scope.edit = edit;
        loadNotify();

        Restangular.one("/api/class/get-by-group").get().then(function (response) { $scope.lstClass = response.data; });
        

        function loadNotify(){
            Restangular.one("/api/notify/getAll").get().then(function (response) { $scope.lstNotify = response.data; });
        }

        function submitNotify() {  
            Restangular.all('/api/notify/insert').post($scope.notify).then(function (response) {
                if(response.code == 200){
                    toastr.success(response.message);
                    loadNotify();
                    $("#modalAddNotify").modal("hide");
                }else{
                    toastr.error(response.message);
                }
            });
        }

        function remove(id){
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.value) {
                    Restangular.one('/api/notify/delete', id).get().then(function (response) {
                        if (response.code == 200) {
                            toastr.success(response.message);
                            loadNotify();
                        }else{
                            toastr.error(response.message);
                        }
                    });
                }
            })
        }

        function edit(id) {
            Restangular.one('/api/notify/get', id).get().then(function (response) {
                if (response.code == 200) {
                    $("#modalAddNotify").modal("show");
                    $scope.notify = response.data;
                }else{
                    toastr.error(response.message);
                }
            });
        }
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
    function DashboardController($scope, $rootScope, Restangular, ChatService) {

    }

    /* ============================================ */
    function ChatController($scope, userName, Restangular, $rootScope, $filter, ChatService) {
        $scope.sendMessage = sendMessage;
        $scope.pressSend = pressSend;

        function pressSend(event) {
            if(event.keyCode == 13) sendMessage();
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
            ChatService.sendDirect(JSON.stringify({'text': $scope.messageContent, 'to': userName}));
            $scope.messageContent = "";
        }

        ChatService.receive().then(null, null, function(message) {
            var time = $filter('date')(new Date(message.time),'yyyy-MM-dd HH:MM:ss');
            var html = '<div class="direct-chat-msg right">';
            html += '<div class="direct-chat-infos clearfix">';
            html += '<span class="direct-chat-name float-right">' + message.fullName +'</span>';
            html += '<span class="direct-chat-timestamp float-left">'+ time +'</span></div>';
            html += '<img class="direct-chat-img" src="'+ message.avatar +'">';
            html += '<div class="direct-chat-text">'+message.text+'</div>';
            $("#box-chat").append(html);
        });
    }

    /* ============================================ */
    function SubjectController($scope, Restangular) {
        $scope.submitSubject = submitSubject;
        $scope.remove = remove;

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

        function remove(id){
            Restangular.one('/api/subject/delete', id).get().then(function (response) {
                if (response.code == 200) {
                    toastr.success(response.message);
                    loadLstSubject();
                }else{
                    toastr.error(response.message);
                }
            });
        }
    }

    /* ============================================ */
    function TeacherController($scope, $http, Restangular) {
        $scope.submitUser = submitUser;
        $scope.remove = remove;

        loadLstTeacher();
        function loadLstTeacher() {
            Restangular.one("/api/organization/get-teacher").get().then(function (response) {$scope.lstTeacher = response.data;});
        }

        function submitUser() {
            Restangular.all('/sign-up?type=teacher').post($scope.user).then(function (response) {
                if(response.code == 200){
                    loadLstTeacher();
                    $("#modalCreateUser").modal("hide");
                    toastr.success(response.message);
                    $scope.user = {};
                }else{
                    toastr.error(response.message);
                }
            }, function(response) {
                toastr.error(response.data.message);
            });
        }

        function remove(teacherId, orgId){
            $http.get("/api/organization/delete-teacher?teacher_id="+ teacherId ).then(function (response) {
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

    /* ============================================ */
    function ParentController($scope, Restangular) {
        $scope.orgId = "";
        Restangular.one("/api/organization/get-parent").get().then(function (response) { $scope.lstParent = response.data; });
    }

    /* ============================================ */
    function PointController($scope,$location, Restangular) {
        Restangular.one("/api/group-class/get-all").get().then(function (response) { $scope.lstGroup = response.data; });
        Restangular.one('/api/subject/getAll').get().then(function (response) { $scope.lstSubject = response.data; });
        Restangular.one("/api/class/get-by-group").get().then(function (response) { $scope.lstClass = response.data; });

        $scope.selectGroup = function() {
            Restangular.one("/api/class/get-by-group?id=" + $scope.groupId).get().then(function (response) { $scope.lstClass = response.data; });
        }
        $("#jsGrid").jsGrid({
            width: "100%",
           // height: "400px",
           paging: true,
           autoload: true,
           pageSize: 10,
           editing: true,
            // rowClick: function(args) {
            //     $scope.notify = args.item;
            //     $scope.$digest();
            //     $("#modalAddNotify").modal("show");
            // },
            confirmDeleting: false,
            onItemDeleting: function (args) {
                if (!args.item.deleteConfirmed) { // custom property for confirmation
                    args.cancel = true; // cancel deleting
                    Swal.fire({
                        title: 'Are you sure?',
                        text: "You won't be able to revert this!",
                        icon: 'warning',
                        showCancelButton: true,
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#d33',
                        confirmButtonText: 'Yes, delete it!'
                    }).then((result) => {
                        if (result.value) {
                            Restangular.one('/api/notify/delete', args.item.id).get().then(function (response) {
                                if (response.code == 200) {
                                    toastr.success(response.message);
                                    $("#jsGrid").jsGrid("loadData");
                                }else{
                                    toastr.error(response.message);
                                }
                            });                            
                        }
                    })
                }
            },
            controller: {
                loadData: function() {
                    var d = $.Deferred();
                    Restangular.one("/api/notify/getAll").get().then(function (response) { 
                        d.resolve(response.data);
                    });
                    return d.promise();
                },
                updateItem: function(item) {
                    var d = $.Deferred();
                    Restangular.one('/api/notify/delete', item.id).get().then(function (response) {
                        if (response.code == 200) {
                            toastr.success(response.message);
                            $("#jsGrid").jsGrid("loadData");
                            d.resolve(response.data);
                        }else{
                            toastr.error(response.message);
                            d.reject();
                        }
                    }, function () {
                        d.reject();
                    });  
                    return d.promise(); 
                }
            },
            fields: [
                { name: "id", title: "ID",},
                { name: "title", title: "Tiêu đề thông báo", type: "text"},
                { name: "content", title: "Nội dung thông báo"},
                { name: "createdDate", title: "Thời gian tạo"},
                { name: "createdName", title: "Người tạo"},
                { name: "status", title: "Trạng thái"},
                { type: "control", deleteButton: false}
            ]
        });
    }
})();