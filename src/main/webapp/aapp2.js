var app = angular.module('plunker', []).controller('Tinytwitter', ['$scope', '$window',
  function($scope, $window) {
    $scope.user;
    $scope.posttime = 0;
    $scope.gettime = 0;
    $scope.gettime2 = 0;
    $scope.tweets = [];
    $scope.hashtag = [];
    
   
    
    $scope.getAsh = function() {
    	 var t0 = performance.now();
        gapi.client.tinytweeter.get_hashtag_tweets({
        	hashtagID: $scope.hash
        	}).execute(
          function(resp) {
            console.log(resp.items);
            $scope.hashtag = resp.items;
            var t1 = performance.now();
            $scope.gettime = (t1 - t0);
            $scope.$apply();
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
