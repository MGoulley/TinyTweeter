var app = angular.module('plunker', []).controller('Tinytwitt', ['$scope', '$window',
  function($scope, $window) {


    $scope.user;
    $scope.posttime = 0;
    $scope.gettime = 0;
    $scope.tweets = [];

    $scope.follow = function() {
      gapi.client.messages.follow_user({
        name: $scope.followed,
        follower: $scope.follower
      }).execute(
        function(resp) {
          console.log(resp);
          $window.alert(resp.content);
        });
    }
    $scope.followByName = function(followedname, followername) {
      gapi.client.messages.follow_user({
        name: followedname,
        follower: followername
      }).execute(
        function(resp) {
          console.log(resp);
          if (resp.code == 503) {
            $scope.followByName(followedname, followername);
          }
        });
    }

    $scope.getUser = function(username) {
      gapi.client.messages.get_user({
        name: username
      }).execute(
        function(resp) {
          $scope.user = resp;
          console.log(resp);
        });

    }

    $scope.addUser = function() {
      gapi.client.messages.add_user({
        user: $scope.adduser
      }).execute(
        function(resp) {
          console.log(resp);
          $window.alert(resp.content);
        });
    }

    $scope.addUserByName = function(username) {
      gapi.client.messages.add_user({
        user: username
      }).execute(
        function(resp) {
          console.log(resp);
        });

    }

    $scope.sendMsg = function() {
      console.time('sendMsg');
      var t0 = performance.now();
      $scope.getUser($scope.sender);
      var sdr = $scope.user;
      gapi.client.messages.add_message({
          sender: $scope.sender,
          receivers: sdr.followers,
          value: $scope.message
        })
        .execute(
          function(resp) {
            console.log(resp);
            //$window.alert(resp.content);
            var t1 = performance.now();
            $scope.posttime = (t1 - t0);
            $scope.$apply();
          });

    }

    $scope.getMsg = function() {
      gapi.client.messages.get_messages({
          user: $scope.receiver
        })
        .execute(
          function(resp) {
            console.log(resp.items);
            $scope.tweets = resp.items;
            $scope.$apply();
          });

    }

    // little hack to be sure that apis.google.com/js/client.js is loaded
    // before calling
    // onload -> init() -> window.init() -> then here
    $window.init = function() {
      console.log("Init called");
      var rootApi = 'https://webclouddist.appspot.com/_ah/api/';
      gapi.client.load('messages', 'v1', function() {
        console.log("API Loaded");
        $scope.getUser("foo");
      }, rootApi);
    }
  }
]);
