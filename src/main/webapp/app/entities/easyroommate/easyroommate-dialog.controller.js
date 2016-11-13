(function() {
    'use strict';

    angular
        .module('roommateApp')
        .controller('EasyroommateDialogController', EasyroommateDialogController);

    EasyroommateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Easyroommate'];

    function EasyroommateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Easyroommate) {
        var vm = this;

        vm.easyroommate = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.easyroommate.id !== null) {
                Easyroommate.update(vm.easyroommate, onSaveSuccess, onSaveError);
            } else {
                Easyroommate.save(vm.easyroommate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('roommateApp:easyroommateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
