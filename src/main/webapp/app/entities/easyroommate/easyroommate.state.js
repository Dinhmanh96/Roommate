(function() {
    'use strict';

    angular
        .module('roommateApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('easyroommate', {
            parent: 'entity',
            url: '/easyroommate',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'roommateApp.easyroommate.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/easyroommate/easyroommates.html',
                    controller: 'EasyroommateController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('easyroommate');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        
        
        
        .state('easyroommate-search', {
            parent: 'entity',
            url: '/easyroommate/search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'roommateApp.easyroommate.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/easyroommate/easyroommate-search.html',
                    controller: 'EasyroommateSearchController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('easyroommate');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        
        .state('easyroommate-map', {
            parent: 'entity',
            url: '/easyroommate/{id}/map',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'roommateApp.easyroommate.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/easyroommate/easyroommate-map.html',
                    controller: 'EasyroommateMapController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('easyroommate');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Easyroommate', function($stateParams, Easyroommate) {
                    return Easyroommate.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'easyroommate',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        
        .state('easyroommate-detail', {
            parent: 'entity',
            url: '/easyroommate/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'roommateApp.easyroommate.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/easyroommate/easyroommate-detail.html',
                    controller: 'EasyroommateDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('easyroommate');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Easyroommate', function($stateParams, Easyroommate) {
                    return Easyroommate.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'easyroommate',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('easyroommate-detail.edit', {
            parent: 'easyroommate-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/easyroommate/easyroommate-dialog.html',
                    controller: 'EasyroommateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Easyroommate', function(Easyroommate) {
                            return Easyroommate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('easyroommate.new', {
            parent: 'easyroommate',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/easyroommate/easyroommate-dialog.html',
                    controller: 'EasyroommateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                address: null,
                                phone: null,
                                rent_per_a_month: null,
                                property_type: null,
                                age_range: null,
                                gender: null,
                                more_infomation: null,
                                search: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('easyroommate', null, { reload: 'easyroommate' });
                }, function() {
                    $state.go('easyroommate');
                });
            }]
        })
        .state('easyroommate.edit', {
            parent: 'easyroommate',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/easyroommate/easyroommate-dialog.html',
                    controller: 'EasyroommateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Easyroommate', function(Easyroommate) {
                            return Easyroommate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('easyroommate', null, { reload: 'easyroommate' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('easyroommate.delete', {
            parent: 'easyroommate',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/easyroommate/easyroommate-delete-dialog.html',
                    controller: 'EasyroommateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Easyroommate', function(Easyroommate) {
                            return Easyroommate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('easyroommate', null, { reload: 'easyroommate' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
