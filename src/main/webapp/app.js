angular.module('helloworld', ['ui.router']).config(function($stateProvider) {
  var helloState = {
    name: 'hello',
    url: '/hello',
    template: '<h3>hello world!</h3>'
  }
  
  var aboutState = {
    name: 'about',
    url: '/about',
    template: '<h3>Its the UI-Router hello world app!</h3>'
  }
  
  $stateProvider.state(helloState);
  $stateProvider.state(aboutState);
});

angular.module('plunker', ['helloworld']).controller('Tinytwitter', ['$scope', '$window',
  function($scope, $window) {
    $scope.user;
    $scope.posttime = 0;
    $scope.gettime = 0;
    $scope.tweets = [];
    
    
    $scope.addUser = function() {
      gapi.client.tinytweeter.create_user({
        username: $scope.adduser
      }).execute(
        function(resp) {
          console.log(resp);
          $window.alert(resp.content);
        });
    }
    
    $scope.follow = function() {
        gapi.client.tinytweeter.add_follow({
        	userID: $scope.followed,
        	followID: $scope.follower
        }).execute(
          function(resp) {
            console.log(resp);
            $window.alert(resp.content);
          });
      }
    
    $scope.sendMsg = function() {
        console.time('sendMsg');
        var t0 = performance.now();
        
        gapi.client.tinytweeter.create_tweet({
        	authorID: $scope.senderID, 
        	authorUsername: $scope.sender, 
        	message: $scope.message
        	}).execute(
          function(resp) {
            console.log(resp);
            var t1 = performance.now();
            $scope.posttime = (t1 - t0);
            $scope.$apply();
          });

      }
    
    $scope.getMsg = function() {
    	 var t0 = performance.now();
        gapi.client.tinytweeter.timeline({
        	userID: $scope.receiver
        	}).execute(
          function(resp) {
            console.log(resp.items);
            $scope.tweets = resp.items;
            var t1 = performance.now();
            $scope.gettime = (t1 - t0);
            $scope.$apply();
          });

      }
    
    $scope.connection = function() {
        gapi.client.tinytweeter.create_user({
        username: $scope.pseudo
      }).execute(
        function(resp) {
            $scope.user = resp
            console.log($scope.user);
            console.log($scope.user.utilisateurID);
            console.log($scope.user.username);
            $window.location.href = '/html2.html';
        });
      }

    $window.init = function() {
      var rootApi = 'http://localhost:8080/_ah/api';
      gapi.client.load('tinytweeter', 'v1', function() {
        console.log("API Loaded");
      }, rootApi);
    }
  }
]);

