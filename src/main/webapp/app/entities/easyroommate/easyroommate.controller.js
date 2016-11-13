(function() {
    'use strict';

    angular
        .module('roommateApp')
        .controller('EasyroommateSearchController', EasyroommateController)
        .controller('EasyroommateController', EasyroommateController);

    EasyroommateController.$inject = ['$scope', '$state', 'Easyroommate', 'EasyroommateSearch'];

    function EasyroommateController ($scope, $state, Easyroommate, EasyroommateSearch) {
        var vm = this;
        
        vm.easyroommates = [];
        vm.search = search;
        vm.loadAll = loadAll;

       loadAll();

        function loadAll() {
            Easyroommate.query(function(result) {
                vm.easyroommates = result;
            });
        }

        function search () {
            if (vm.searchQuery) {
            	EasyroommateSearch.query({query: vm.searchQuery}, function(result) {
                    vm.easyroommates = result;
                });
            }
           
            return vm.loadAll();
        }
   
   
      }
})();
