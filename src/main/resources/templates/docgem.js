var docgem = angular.module('docgem', ['ngResource','ngRoute','ngSanitize']);


docgem.controller('DocgemCtrl',['$q','$scope','$rootScope', '$location', '$route','$routeParams', function($q, $scope,$rootScope,$location,$route,$routeParams) {

  $rootScope.asideScope = $scope;
  $scope.revisao = new Date();
  $scope.titel = 'Manual';

  $scope.chapters = [
  	{
  		title:'Programa',
  		indice: '1',
  		path: 'paginas/capitulo1.html',
        sections: [
          {
            title:'categoria de Investimento',
            indice: '1.1',
            path: 'paginas/capitulo1_1.html',
            sections: [
              {
                title:'Criando Categoria de Investimento',
                indice: '1.1.1',
                path: 'paginas/capitulo1_1_1.html',
                actions: [
                	{
                		text: 'Click em 1',
                		imageFile: 'images/1180930.png'
                	},
                	{
                		text: 'Click em 2',
                		imageFile: 'images/1442739.png'
                	},
                	{
                		text: 'Click em 3',
                		imageFile: 'images/1842580.png'
                	},
                	{
                		text: 'Click em 4',
                		imageFile: 'images/3053458.png'
                	},
                	{
                		text: 'Click em 5',
                		imageFile: 'images/3073320.png'
                	}
                ]
              },
              {
                title:'Editando Categoria de Investimento',
                indice: '1.1.2',
                path: 'paginas/capitulo1_1_2.html',
              },
              {
                title:'Excluindo Categoria de Investimento',
                indice: '1.1.2',
                path: 'paginas/capitulo1_1_3.html',
              }
            ]
          },
          {
            title:'Contrato de Empréstimo',
            indice: '1.2',
            path: 'paginas/capitulo1_2.html',
            sections: [
              {
                title:'Criando Contrato de Empréstimo',
                indice: '1.2.1',
                path: 'paginas/capitulo1_2_1.html',
              },
              {
                title:'Editando Contrato de Empréstimo',
                indice: '1.2.2',
                path: 'paginas/capitulo1_2_2.html',
              },
              {
                title:'Excluindo Contrato de Empréstimo',
                indice: '1.2.3',
                path: 'paginas/capitulo1_2_3.html',
              }
            ]
          }
        ]
  	},
  	{
  		title:'Financeiro',
  		indice: '2',
  		path: 'capitulo2.html',
        sections: [
          {
            title:'Cadastros',
            indice: '2.1',
            path: 'capitulo1.html',
            sections: [
                {
                  title:'Fonte de Recursos',
                  indice: '2.1.1',
                  path: 'capitulo2.html',
                  sections: [
                    {
                      title:'Criando Fonte de Recurso',
                      indice: '2.1.1.1',
                      path: 'capitulo.html',
                    },
                    {
                      title:'Editando Fonte de Recurso',
                      indice: '2.1.1.2',
                      path: 'capitulo.html',
                    },
                    {
                      title:'Excluindo Fonte de Recurso',
                      indice: '2.1.1.3',
                      path: 'capitulo.html',
                    }
                  ]
                }
            ]
          }

        ]
  	},
  ];

  $scope.currentItem = $scope.chapters[0];

  $scope.selectItem = function(item) {
    $scope.currentItem = item;
    item.visited = true;
    if($scope.currentItem.actions) {
    	$scope.action = $scope.currentItem.actions[0];
    } else {
    	$scope.action = undefined;
    }
  };

  $scope.getVisitedClass = function(item) {
    if(item.visited) {
      return 'done';
    } else {
      return '';
    }
  };

  $scope.itemUrl = function () {
    var url;
    if($scope.currentItem) {
      url = $scope.currentItem.path;
    }
    return url;
  };

  $scope.hasPreviousAction = function() {
    if($scope.currentItem.actions && $scope.action) {
      return $scope.currentItem.actions.indexOf($scope.action) > 0;
    }
    return false;
  };

  $scope.hasNextAction = function() {
    if($scope.currentItem.actions && $scope.action) {
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

}]);
