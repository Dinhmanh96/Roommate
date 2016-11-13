(function() {
    'use strict';
    angular
        .module('roommateApp')
        .factory('Easyroommate', Easyroommate);

    Easyroommate.$inject = ['$resource'];

    function Easyroommate ($resource) {
        var resourceUrl =  'api/easyroommates/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
