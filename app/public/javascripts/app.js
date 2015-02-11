var app = angular.module("TriangleApp", ['ngCookies']).run(function($rootScope, $cookieStore, Session, AUTH_EVENTS) {

    $rootScope.isAuthenticated = Session.isAuthenticated();

    $rootScope.$on(AUTH_EVENTS.loginFailed, function() {
        $rootScope.isAuthenticated = false;
    });

    $rootScope.$on(AUTH_EVENTS.logoutSuccess, function() {
        $rootScope.isAuthenticated = false;
    });

    $rootScope.$on(AUTH_EVENTS.loginSuccess, function() {
        $rootScope.isAuthenticated = true;
    });
});

app.constant('AUTH_EVENTS', {
    loginSuccess: 'auth-login-success',
    loginFailed: 'auth-login-failed',
    logoutSuccess: 'auth-logout-success'
});

app.constant('APP', {
    authToken: 'AuthToken',
    username: 'Username'
});

app.config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('SecurityInterceptor');
}]);

app.service('Session', function ($cookieStore, APP) {
    this.create = function (token, username) {
        $cookieStore.put(APP.authToken, token);
        $cookieStore.put(APP.username, username);
    };

    this.destroy = function () {
        $cookieStore.remove(APP.username);
        $cookieStore.remove(APP.authToken);
    };

    this.isAuthenticated = function () {
        return $cookieStore.get(APP.authToken) != undefined;
    }

    return this;
});

app.controller('applicationController', function ($scope, $http, AUTH_EVENTS) {

    $scope.books = [];

    $scope.$on(AUTH_EVENTS.logoutSuccess, function() {
        $scope.books = [];
    });

    $scope.getBooks = function() {
        $http.get("/api/books").success(function(data, status) {
            $scope.books = data;
        });
    }
});

app.factory('AuthService', function ($http, Session) {
    var authService = {};
    authService.login = function (credentials) {
        return $http
            .post('/api/login', credentials)
            .success(
                function (res, status, headers) {
                    if(status == 200) {
                        Session.create(res.token, res.email);
                    }
                })
            .error(
                function(res) {
                    alert("Error calling login");
                }
            );
    };
    authService.signoff = function() {
        Session.destroy();
    }
    return authService;
});

app.factory('SecurityInterceptor',function($cookieStore, APP ) {

    var SecurityInterceptor = {
        request: function(config) {
            config.headers['Security'] =  $cookieStore.get(APP.authToken);
            return config;
        },
        response: function(response) {
            return response;
        },
        responseError: function(response) {
            if(response.status == 419) {
                //expired
            }
            if(response.status == 401) {
                //forbidden
                alert('Unauthorized');
            }
            return response;
        }
    };
    return SecurityInterceptor;
});

app.controller("loginController", function($scope, $rootScope, $http, AUTH_EVENTS, AuthService){
    $scope.credentials = {
        email: '',
        password: ''
    };
    $scope.login = function (credentials) {
        AuthService.login(credentials).then(function (user) {
            $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
        }, function () {
            $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
        });
    };
    $scope.signoff = function() {
        AuthService.signoff();
        $rootScope.$broadcast(AUTH_EVENTS.logoutSuccess);
    }
});
