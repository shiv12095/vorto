define(["angular"],function(angular) {
var app = angular.module('apps.controller', ['smart-table','toastr']);

app.controller('InfomodelEditorController', function($rootScope, $scope, $http, $location) {
	
	$scope.editor = null;
	$scope.error = null;
	$scope.errorMessage = null;
	$scope.validationError = false;
	$scope.selectedModel = null;
	$scope.selectedModelId = null;
	$scope.enableAddFunctionBlockButton = true;
	
	$scope.models = [];
  	$scope.queryFilter = "";	
	
	require(["webjars/ace/1.2.0/src/ace"], function() {
				require(["xtext/xtext-ace"], function(xtext) {
					editor = xtext.createEditor({syntaxDefinition: "xtext/mode-infomodel"});
					console.log('Editor created now');
				});
			});
	
	$scope.isValidInfomodel = function() {
		if (editor.xtextServices.editorContext._annotations.length != 0) {
			$error = "Your information model contains errors. Please correct and try again.";
			return false;
		}else{
			return true;
		}
	}
			
	$scope.share = function() {
		if ($scope.isValidInfomodel()) {
			// do upload and checkin REST call to Vorto Information Model Repository
		}		
	};
	
  	$scope.searchOnEnter = function(keyEvent) {
  		if (keyEvent.keyCode === 13) {
  			$scope.search();
  		}
  	};

  	$scope.search = function() {
  		var modelType = "Functionblock";
  		var filter = null;
  		filter = $scope.queryFilter + " " + modelType ;
  		$http.get('./editor/infomodel/search=' + filter).success(
	      function(data, status, headers, config) {
					$scope.models = data;
	      }).error(function(data, status, headers, config) {
					$scope.models = [];
	      });
		};

	$scope.isFunctionBlockSelected = function() {
		for (i = 0; i < $scope.displayedModels.length; i++) { 
		    if($scope.displayedModels[i]['isSelected']){
		    	$scope.selectedModel = $scope.displayedModels[i];
		    	$scope.selectedModelId = $scope.selectedModel['id'];
		    	return true;
		    }
		}
    	$scope.selectedModel = null;
    	$scope.selectedModelId = null;
		return false;
	}
		
	$scope.importFunctionBlock = function() {
		if($scope.isFunctionBlockSelected()){
			if($scope.isValidInfomodel()){
				$scope.enableAddFunctionBlockButton = false
		  		$http.get('./editor/infomodel/add/functionblock/' +  $scope.selectedModelId['namespace'] + '/' + $scope.selectedModelId['name'] + '/' + $scope.selectedModelId['version'] ).success(
		  		      function(data, status, headers, config) {
		  		    	  window.alert('Added successfully')
		  		    	  $scope.updateInfoModel();
		  		      }).error(function(data, status, headers, config) {
		  		    	  window.alert('Failed')
		  		      });
				$scope.enableAddFunctionBlockButton = true;
			}
			else{
				window.alert('Your information model contains errors. Please correct and try again.');			
			}
		}else{
			window.alert('Please select a function block');			
		}
	}
	
	$scope.updateInfoModel = function() {
		var originalText = editor.getValue();
		var pos = originalText.indexOf('infomodel');
		var newText = originalText.substr(0, pos-1) + "using " + $scope.selectedModelId['namespace'] + '.' + $scope.selectedModelId['name'] +  " ; " + $scope.selectedModelId['version'] + "\n\n" + originalText.substr(pos);
		editor.setValue(newText);
	}
			
  	$scope.search();

    $scope.displayedModels = [].concat($scope.models);
    $scope.itemsByPage 		= 6;
  	$scope.displayedPages 	= ($scope.models.length / 2);

  	$scope.getters= {
        namespace: function (value) {
	        return value.id.namespace.sort();
        },
        name: function (value) {
	        return value.id.name.sort();
        },
        version: function (value) {
	        return value.id.version.sort();
        }
  	 }
  	
  	$scope.predicates = ['Name', 'Namespace', 'Version'];
  	
});

return app;
});
