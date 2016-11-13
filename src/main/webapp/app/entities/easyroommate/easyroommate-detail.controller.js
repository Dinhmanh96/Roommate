(function() {
    'use strict';

    angular
        .module('roommateApp')
        .controller('EasyroommateMapController', EasyroommateDetailController)
        .controller('EasyroommateDetailController', EasyroommateDetailController);

    EasyroommateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Easyroommate'];

    function EasyroommateDetailController($scope, $rootScope, $stateParams, previousState, entity, Easyroommate) {
        var vm = this;

        vm.easyroommate = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('roommateApp:easyroommateUpdate', function(event, result) {
            vm.easyroommate = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
