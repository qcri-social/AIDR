var app = angular.module('DataApp',
		[ 'ngRoute', 'ngResource', 'ui.bootstrap', 'ngAnimate' ]).run(
		function($rootScope) {
			$rootScope.authenticated = false;
			$rootScope.current_user = '';
		});

app.controller('appCtrl', function($scope, $uibModal, $log, $filter, $http) {
	$http.get('/aidr-data/dashboard/list').then(function(result) {
		$scope.alphabet = result.data;
		$scope.buildPager();

	});
	$scope.isNull = function(value) {

		if (value == null)
			return true;
		else
			return false;

	};
$scope.null = function(value) {

  return false;


};
	$scope.items = [ 'item1', 'item2', 'item3' ];

	/*
	 * functions to create client side pagination
	 */
	$scope.buildPager = function() {
		$scope.pagedItems = [];
		$scope.itemsPerPage = 9;
		$scope.currentPage = 1;
		$scope.figureOutItemsToDisplay();
	};

	$scope.figureOutItemsToDisplay = function() {
		$scope.filteredItems = $filter('filter')($scope.alphabet, {
			$ : $scope.search
		});
		$scope.filterLength = $scope.filteredItems.length;
		var begin = (($scope.currentPage - 1) * $scope.itemsPerPage);
		var end = begin + $scope.itemsPerPage;
		$scope.pagedItems = $scope.filteredItems.slice(begin, end);
	};

	$scope.pageChanged = function() {
		$scope.figureOutItemsToDisplay();
	};

	$scope.animationsEnabled = true;
	$scope.open = function(letter) {
		var modalInstance = $uibModal.open({
			animation : $scope.animationsEnabled,
			templateUrl : 'myModalContent.html',
			controller : 'ModalInstanceCtrl',
			windowClass : 'center-modal',
			size : 'lg',
			scope : $scope,
			resolve : {
				items : function() {
					return letter;
				}
			}
		});

		modalInstance.result.then(function(selectedItem) {
			$scope.selected = selectedItem;
		}, function() {
			$log.info('Modal dismissed at: ' + new Date());
		});
	};

	$scope.toggleAnimation = function() {
		$scope.animationsEnabled = !$scope.animationsEnabled;
	};

});
angular.module('DataApp').controller(
		'ModalInstanceCtrl',
		function($scope, $http, $uibModalInstance, items) {
			console.log(items);
			$scope.collection = items;
			$scope.downloadTweets = function() {
			    $scope.busy = true;
				/*
				 * the $http service allows you to make arbitrary ajax requests.
				 * in this case you might also consider using angular-resource
				 * and setting up a User $resource.
				 */
				console.log("closed");
				$http.post(
						'/aidr-data/persister/generateDownloadLink?' + 'code='
								+ $scope.collection.code + '&count=50000'
								+ '&removeRetweet=false' + '&createdTimestamp='
								+ $scope.collection.collectionCreationDate
								+ '&type=CSV').then(function(response) {
									
					$scope.busy = false;
					$scope.results = response;
					window.open($scope.results.data.data);
				}, function(failure) {
					console.log("failed :(", failure);
				});

			};

			$scope.cancel = function() {
				$uibModalInstance.dismiss('cancel');
			};
		});
