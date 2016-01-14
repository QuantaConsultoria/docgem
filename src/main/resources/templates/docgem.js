var docgem = angular.module('docgem', ['ngResource','ngRoute','ngSanitize', 'angularResizable']);

var autocomplete = {};

autocomplete.feedJsonListForSearch = function(sectionList, jsonList){
  for(var s in sectionList){
    if(sectionList[s].sections && sectionList[s].sections.length)
      autocomplete.feedJsonListForSearch(sectionList[s].sections, jsonList);

    jsonList.push(sectionList[s]);
  }
};

autocomplete.init = function(data, scope){
  var jsonList = [];
  autocomplete.feedJsonListForSearch(data, jsonList);
  autocomplete.itens = jsonList;
  autocomplete.scope = scope;
};

autocomplete.map = {};

autocomplete.$ = function(queryLocator){
  autocomplete.map = {};

  $(queryLocator).typeahead({
    source: function(query, process){
      var itens = _.filter(autocomplete.itens, function(item){
        return item.title.toLowerCase().indexOf(query.toLowerCase()) != -1 ||
               (item.content && item.content.toLowerCase().indexOf(query) != -1);
      });

      itens = _.map(itens, function(item){
        var content = item.content;
        if(content){
          var index = content.toLowerCase().indexOf(query.toLowerCase());
          content = content.substring(index - 20, index + 20);
        }
        content = item.title + ";" + (content ? content : "");
        autocomplete.map[content] = item;
        return content;
      });

      process(itens);
    },
    highlighter: function(item){
      var texts = item.split(";");

      var template = "<strong>" + texts[0] + "</strong>";
      var subtitle = texts[1];

      if(subtitle)
        template += "<p style='font-size: 8pt;'>..." + subtitle + "...</p>";

      return template;
    },
    updater: function(item){
      autocomplete.scope.$apply(
        function() {
          autocomplete.scope.selectItem(autocomplete.map[item]);
          autocomplete.scope.toggleSearch();
        }
      );
      return item;
    },
    items: 5
  });
}

docgem.controller('DocgemCtrl',['$q','$scope','$rootScope', '$location', '$route','$routeParams', '$http', '$timeout', function($q, $scope,$rootScope,$location,$route,$routeParams, $http, $timeout) {

  $http.get('data.js').success(function(data) {
	  $scope.chapters = data.chapters.sort(function(a,b){
      return parseInt(a.indice) - parseInt(b.indice);
    });

    if($scope.chapters && $scope.chapters.length)
      $scope.currentItem = $scope.chapters[0];

    autocomplete.init(data.chapters, $scope);
    autocomplete.$('.autocomplete');
  });

  $rootScope.asideScope = $scope;
  $scope.revisao = new Date();
  $scope.titel = 'Manual';
  $scope.showSummary = true;
  $scope.showSearch = false;

  $scope.selectItem = function(item) {
    $scope.currentItem = item;
    item.visited = true;
    if($scope.currentItem.actions) {
    	$scope.action = $scope.currentItem.actions[0];
    } else {
    	$scope.action = undefined;
    }
  };

  $scope.toggleSearch = function(){
    $('.autocomplete').val('');
    $scope.showSearch = !$scope.showSearch;
    if($scope.showSearch){
      $timeout(function(){
        $('.autocomplete').focus();
      }, 100);
    }
  }

  $scope.getVisitedClass = function(item) {
    if(item.visited) {
      return 'done';
    } else {
      return '';
    }
  };

  $scope.applyTreeviewStyle = function(){
    $timeout(function(){
      $('.summary').treed();
    }, 500);
  }

  $scope.itemUrl = function () {
    var url;
    if($scope.currentItem) {
      url = $scope.currentItem.path + ".html";
    }
    return url;
  };

  $scope.hasPreviousAction = function() {
    if($scope.currentItem && $scope.currentItem.actions && $scope.action) {
      return $scope.currentItem.actions.indexOf($scope.action) > 0;
    }
    return false;
  };

  $scope.hasNextAction = function() {
    if($scope.currentItem && $scope.currentItem.actions && $scope.action) {
      return $scope.currentItem.actions.indexOf($scope.action) < $scope.currentItem.actions.length -1;
    }
    return false;
  };

  $scope.previousAction = function() {
    var indexAtual = $scope.currentItem.actions.indexOf($scope.action);
    $scope.action = $scope.currentItem.actions[indexAtual - 1];
  };

  $scope.nextAction = function() {
    var indexAtual = $scope.currentItem.actions.indexOf($scope.action);
    $scope.action = $scope.currentItem.actions[indexAtual + 1];
  };

  $scope.nextPage = function(){

  };

  $scope.previousPage = function(){

  };

  $scope.toggleSummary = function(){
    $scope.showSummary = !$scope.showSummary;
  };

}]);
