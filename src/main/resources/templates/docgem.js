var docgem = angular.module('docgem', ['ngResource','ngRoute','ngSanitize']);


docgem.controller('DocgemCtrl',['$q','$scope','$rootScope', '$location', '$route','$routeParams', function($q, $scope,$rootScope,$location,$route,$routeParams){


    $rootScope.asideScope = $scope;
    $scope.revisao = new Date();
    $scope.titel = 'Manual';

    $scope.chapters = [
    	{
    		title:'Programa',
    		indice: '1',
    		path: 'capitulo.html',
            sections: [
                {
                    title:'categoria de Investimento',
                    indice: '1.1',
                    path: 'capitulo1.html',
                    sections: [
                        {
                            title:'Criando Categoria de Investimento',
                            indice: '1.1.1',
                            path: 'capitulo.html',
                        },
                        {
                            title:'Editando Categoria de Investimento',
                            indice: '1.1.2',
                            path: 'capitulo.html',
                        },
                        {
                            title:'Excluindo Categoria de Investimento',
                            indice: '1.1.2',
                            path: 'capitulo.html',
                        }
                    ]
                },
                {
                    title:'Contrato de Empréstimo',
                    indice: '1.2',
                    path: 'capitulo1.html',
                    sections: [
                        {
                            title:'Criando Contrato de Empréstimo',
                            indice: '1.2.1',
                            path: 'capitulo.html',
                        },
                        {
                            title:'Editando Contrato de Empréstimo',
                            indice: '1.2.2',
                            path: 'capitulo.html',
                        },
                        {
                            title:'Excluindo Contrato de Empréstimo',
                            indice: '1.2.3',
                            path: 'capitulo.html',
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
}]);
