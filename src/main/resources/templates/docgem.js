var docgem = angular.module('docgem', ['ngResource','ngRoute','ngSanitize']);


docgem.controller('DocgemCtrl',['$q','$scope','$rootScope', '$location', '$route','$routeParams', function($q, $scope,$rootScope,$location,$route,$routeParams){


    $rootScope.asideScope = $scope;
    $scope.revisao = new Date();
    $scope.titel = 'Manual';

    $scope.itens = [
    	{
    		title:'Capítulo 1',
    		indice: '1',
    		path: 'capitulo.html'
    	},
    	{
    		title:'Sessão 1',
    		indice: '2',
    		path: 'capitulo1.html'
    	},
    	{
    		title:'Sessão 2',
    		indice: '3',
    		path: 'capitulo1.html'
    	},
    	{
    		title:'Capítulo 2',
    		indice: '2',
    		path: 'capitulo2.html'
    	},
    ];

    $scope.currentItem = $scope.itens[0];
}]);
