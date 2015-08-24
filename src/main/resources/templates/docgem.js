var docgem = angular.module('docgem', ['ngResource','directives','ngRoute','ngSanitize','angularFileUpload', 'ngTable']);


docgem.controller('DocgemCtrl',['$q','$scope','$rootScope', '$location', '$route','$routeParams', 'Security', function($q, $scope,$rootScope,$location,$route,$routeParams, Security){


    $rootScope.asideScope = $scope;
    $scope.revisao = new Date();
    $scope.titel = 'Manual';

    $scope.items = [
    	{
    		titel:'Capítulo 1',
    		indice: '1',
    		path: 'capitulo1.html'
    	},
    	{
    		titel:'Sessão 1',
    		indice: '2',
    		path: 'capitulo1.html'
    	},
    	{
    		titel:'Sessão 2',
    		indice: '3',
    		path: 'capitulo1.html'
    	},
    	{
    		titel:'Capítulo 2',
    		indice: '2',
    		path: 'capitulo1.html'
    	},
    ];
}]);
