var app = angular.module('plunker', []).controller('Tinytwitter', ['$scope', '$window',
  function($scope, $window) {
    $scope.user;

    $scope.addUser = function() {
      gapi.client.tinytweeter.create_user({
        username: $scope.adduser
      }).execute(
        function(resp) {
          console.log(resp);
          $window.alert(resp.content);
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
