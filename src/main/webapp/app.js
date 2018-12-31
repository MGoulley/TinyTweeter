var app = angular.module('app', ['ui.router']);

app.controller("controller", function($location){
    $location.path('/accueil')
});

app.factory("factory", function(){
    var username = "";
    var userID = 0;

    function setuser(user){
        username = user.username;
        userID = user.utilisateurID;
    }

    function getusername(){
        return username;
    }

    function getuserid(){
        return userID;
    }

    return{
        setuser: setuser,
        getusername: getusername,
        getuserid: getuserid
    }
});

app.config(function($stateProvider, $urlRouterProvider){
    $stateProvider
    .state('accueil',{
        url: '/accueil',
        templateUrl: '/templates/accueil.html'
    })
    $stateProvider
    .state('utilisateur',{
        url: '/utilisateur',
        templateUrl: '/templates/utilisateur.html'
    })
});

app.controller('Tinytwitter', function ($scope, $location, $window, factory) {
    $scope.user;
    $scope.posttime = 0;
    $scope.gettime = 0;
    $scope.tweets = [];


    $scope.addUser = function () {
        gapi.client.tinytweeter.create_user({
            username: $scope.adduser
        }).execute(
            function (resp) {
                console.log(resp);
                $window.alert(resp.content);
            });
    }

    $scope.follow = function () {
        gapi.client.tinytweeter.add_follow({
            userID: $scope.followed,
            followID: $scope.follower
        }).execute(
            function (resp) {
                console.log(resp);
                $window.alert(resp.content);
            });
    }

    $scope.sendMsg = function () {
        console.time('sendMsg');
        var t0 = performance.now();

        gapi.client.tinytweeter.create_tweet({
            authorID: $scope.senderID,
            authorUsername: $scope.sender,
            message: $scope.message
        }).execute(
            function (resp) {
                console.log(resp);
                var t1 = performance.now();
                $scope.posttime = (t1 - t0);
                $scope.$apply();
            });

    }

    $scope.getMsg = function () {
        var t0 = performance.now();
        gapi.client.tinytweeter.timeline({
            userID: $scope.receiver
        }).execute(
            function (resp) {
                console.log(resp.items);
                $scope.tweets = resp.items;
                var t1 = performance.now();
                $scope.gettime = (t1 - t0);
                $scope.$apply();
            });

    }

    $scope.connection = function () {
        gapi.client.tinytweeter.create_user({
            username: $scope.pseudo
        }).execute(
            function (resp) {
                $scope.user = resp
                console.log($scope.user);
                console.log($scope.user.utilisateurID);
                console.log($scope.user.username);
                factory.setuser(resp);
                $location.path('/utilisateur')
            });
    }

    $window.init = function () {
        var rootApi = 'http://localhost:8080/_ah/api';
        gapi.client.load('tinytweeter', 'v1', function () {
            console.log("API Loaded");
        }, rootApi);
    }
});

