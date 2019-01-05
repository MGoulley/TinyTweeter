var app = angular.module('app', ['ui.router']);

app.controller("controller", function ($location) {
    $location.path('/accueil')
});

app.controller("utilisateur", function ($scope, $location, factory) {
    $scope.user = factory.getuser();
    $scope.timeuser = factory.gettimeuser();
    $scope.timeline = factory.gettimeline();
    $scope.hashtags = factory.gethashtags();

    $scope.gethashtags = function () {
        console.log('test');
        gapi.client.tinytweeter.hashtags.execute(
            function (resp) {
                console.log(resp.items);
                factory.sethashtags(resp.items); 
            });
    }
});

app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('accueil', {
            url: '/accueil',
            templateUrl: '/templates/accueil.html'
        })
    $stateProvider
        .state('utilisateur', {
            url: '/utilisateur',
            templateUrl: '/templates/utilisateur.html'
        })
});

app.controller('Tinytwitter', function ($scope, $location, $window, factory) {
    $scope.follow = function () {
        gapi.client.tinytweeter.add_follow({
            userID: $scope.followed,
            followID: $scope.follower
        }).execute(
            function (resp) {
                console.log(resp);
            });
    }

    $scope.sendMsg = function () {
        console.log(factory.getuser());
        var t0 = performance.now();
        gapi.client.tinytweeter.create_tweet({
            authorID: factory.getuser().utilisateurID,
            authorUsername: factory.getuser().username,
            message: $scope.message
        }).execute(
            function (resp) {
                console.log(resp);
                $location.path('/utilisateur')
            });

    }

    $scope.getMsg = function () {
        gapi.client.tinytweeter.timeline({
            userID: factory.getuser().utilisateurID,
            nb_messages: 10
        }).execute(
            function (resp) {
                console.log(resp.items);
                factory.settimeline(resp.items); 
            });
    }

    $scope.resetall = function () {
        gapi.client.tinytweeter.resetall().execute();
    }

    $scope.connection = function () {
        var t0 = performance.now();
        gapi.client.tinytweeter.create_user({
            username: $scope.pseudo
        }).execute(
            function (resp) {
                if (resp.code == 400) {
                    console.log("Message vide.");
                } else {
                    factory.settimeuser(performance.now() - t0);
                    factory.setuser(resp);
                    $scope.getMsg();     
                    console.log(resp);
                    $location.path('/utilisateur');
                    $scope.$applyAsync();
                }
            });
    }

    $window.init = function () {
        var rootApi = 'http://localhost:8080/_ah/api';
        gapi.client.load('tinytweeter', 'v1', function () {
            console.log("API Loaded");
        }, rootApi);
    }
});

app.factory("factory", function () {
    var user = {};
    var timeline = {};
    var hashtags = {};

    var timeUser = 0;
    var timeTimeline = 0;

    function settimeuser(temps) {
        timeUser = temps;
    }

    function gettimeuser() {
        return timeUser;
    }

    function settimeTimeline(temps) {
        timeTimeline = temps;
    }

    function gettimeTimeline() {
        return timeTimeline;
    }

    function settimeline(tweets) {
        timeline = tweets;
    }

    function gettimeline() {
        return timeline;
    }

    function setuser(utilisateur) {
        user = utilisateur;
    }

    function getuser() {
        return user;
    }

    function gethashtags() {
        return hashtags;
    }

    function sethashtags(valeur) {
        hashtags = valeur;
    }

    return {
        setuser: setuser,
        getuser: getuser,
        settimeline: settimeline,
        gettimeline: gettimeline,
        settimetimeline: settimeTimeline,
        gettimetimeline: gettimeTimeline,
        settimeuser: settimeuser,
        gettimeuser: gettimeuser,
        gethashtags: gethashtags,
        sethashtags: sethashtags
    }
});