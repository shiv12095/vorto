define("devtoolApp",["angular","angular-route","smart-table","toastr","controllers"],function(angular) {

	var app = angular.module('devtoolApp', ['ngRoute','apps.controller','toastr']);
	
	app.bootstrap = function() {
		angular.bootstrap(document,['devtoolApp']);
	};
	
	app.config([ '$routeProvider', '$httpProvider',
		function($routeProvider, $httpProvider) {
			$routeProvider.when('/editor', {
				templateUrl : "templates/editor-template.html",
				controller : 'InfomodelEditorController'
			}).otherwise({
				redirectTo : '/editor'
			});
			
} ]).run(function($location, $http, $rootScope) {
	// add global functions here
});
	
	return app;
});