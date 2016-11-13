(function() {
    'use strict';

    angular
        .module('roommateApp')
        .controller('EasyroommateDeleteController',EasyroommateDeleteController);

    EasyroommateDeleteController.$inject = ['$uibModalInstance', 'entity', 'Easyroommate'];

    function EasyroommateDeleteController($uibModalInstance, entity, Easyroommate) {
        var vm = this;

        vm.easyroommate = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Easyroommate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
