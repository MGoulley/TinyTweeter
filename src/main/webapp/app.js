var app = angular.module('app', ['ui.router']);

app.controller("controller", function ($location) {
    $location.path('/accueil');
});

app.config(function ($stateProvider) {
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
    $stateProvider
        .state('hashtag', {
            url: '/hashtag',
            templateUrl: '/templates/hashtag.html'
        })
});

app.controller('Tinytwitter', function ($scope, $location, $window) {
    $scope.user = JSON.parse($window.localStorage.getItem('user'));
    $scope.timeline = (JSON.parse($window.localStorage.getItem('timeline')) !== null) ? JSON.parse($window.localStorage.getItem('timeline')) : JSON.parse($window.localStorage.getItem('timeline'));
    $scope.hashtags = (JSON.parse($window.localStorage.getItem('hashtags')) !== null) ? JSON.parse($window.localStorage.getItem('hashtags')) : JSON.parse($window.localStorage.getItem('hashtags'));
    $scope.users = (JSON.parse($window.localStorage.getItem('users')) !== null) ? JSON.parse($window.localStorage.getItem('users')) : JSON.parse($window.localStorage.getItem('users'));

    $scope.timeUser = $window.localStorage.getItem('timeuser');
    $scope.timeTimeline = $window.localStorage.getItem('timeTimeline');
    $scope.timehashtags = $window.localStorage.getItem('timehashtags');
    $scope.timeusers = $window.localStorage.getItem('timeusers');

    $scope.slctht = (JSON.parse($window.localStorage.getItem('slctht')) !== null) ? JSON.parse($window.localStorage.getItem('slctht')) : JSON.parse($window.localStorage.getItem('slctht'));
    $scope.tweetforhashtag = (JSON.parse($window.localStorage.getItem('tweetforhashtag')) !== null) ? JSON.parse($window.localStorage.getItem('tweetforhashtag')) : JSON.parse($window.localStorage.getItem('tweetforhashtag'));

    $scope.getAsh = function () {
        gapi.client.tinytweeter.get_hashtag_tweets({
            hashtagID: $scope.slctht
        }).execute(
            function (resp) {
                $window.localStorage.setItem('tweetforhashtag', JSON.stringify(resp.items));
                $scope.tweetforhashtag = JSON.parse($window.localStorage.getItem('tweetforhashtag'));
            });
    }

    $scope.getMsg = function () {
        gapi.client.tinytweeter.timeline({
            userID: $scope.user.utilisateurID,
            nb_messages: 20
        }).execute(
            function (resp) {
                $window.localStorage.setItem('timeline', JSON.stringify(resp.items));
                $scope.timeline = JSON.parse($window.localStorage.getItem('timeline'));
            });
    }

    $scope.gethashtags = function () {
        gapi.client.tinytweeter.hashtags().execute(
            function (resp) {
                $window.localStorage.setItem('hashtags', JSON.stringify(resp.items));
                $scope.hashtags = JSON.parse($window.localStorage.getItem('hashtags'));
            });
    }

    $scope.getusers = function () {
        gapi.client.tinytweeter.users({
            limite: 10
        }).execute(
            function (resp) {
                $window.localStorage.setItem('users', JSON.stringify(resp.items));
                $scope.users = JSON.parse($window.localStorage.getItem('users'));
            });
    }

    $scope.sendMsg = function () {
        gapi.client.tinytweeter.create_tweet({
            authorID: $scope.user.utilisateurID,
            authorUsername: $scope.user.username,
            message: $scope.message
        }).execute(
            function (resp) {
                $scope.getMsg();
                $scope.pseudo = $scope.user.username;
                $scope.connection();
            });
    }

    $scope.miid = function (hashtagid) {
        $window.localStorage.setItem('slctht', JSON.stringify(hashtagid));
        $scope.slctht = JSON.parse($window.localStorage.getItem('slctht'));
        console.log($scope.slctht);
        $scope.getAsh();
        console.log($scope.tweetforhashtag);
        $location.path('/hashtag');
    };

    $scope.follow = function (followedID) {
        gapi.client.tinytweeter.add_follow({
            userID: $scope.user.utilisateurID,
            followID: followedID
        }).execute(
            function (resp) {
                // "refresh" la page
                $scope.getMsg();
                $scope.pseudo = $scope.user.username;
                $scope.connection();
            });
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
                    $window.localStorage.setItem('user', JSON.stringify(resp));
                    $scope.user = JSON.parse($window.localStorage.getItem('user'));
                    $scope.timeUser = performance.now() - t0;
                    $window.localStorage.setItem('timeuser', JSON.stringify($scope.timeUser));

                    t0 = performance.now();
                    $scope.getMsg();
                    $scope.timeTimeline = performance.now() - t0;
                    $window.localStorage.setItem('timeTimeline', JSON.stringify($scope.timeTimeline));

                    t0 = performance.now();
                    $scope.gethashtags();
                    $scope.timehashtags = performance.now() - t0;
                    $window.localStorage.setItem('timehashtags', JSON.stringify($scope.timehashtags));

                    t0 = performance.now();
                    $scope.getusers();
                    $scope.timeusers = performance.now() - t0;
                    $window.localStorage.setItem('timeusers', JSON.stringify($scope.timeusers));

                    $location.path('/utilisateur')
                    $scope.$apply();
                }
            });
    }

    $window.init = function () {
        $window.localStorage.clear(); //refresh la session lors de la connection
        var rootApi = 'http://tinytweet.appspot.com/_ah/api';
        // var rootApi = 'http://localhost:8080/_ah/api';
        gapi.client.load('tinytweeter', 'v1', function () {
            console.log("API Loaded");
        }, rootApi);
    }
});