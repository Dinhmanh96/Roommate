(function() {
    'use strict';

    angular
        .module('roommateApp')
        .factory('EasyroommateSearch', EasyroommateSearch);

    EasyroommateSearch.$inject = ['$resource'];

    function EasyroommateSearch($resource) {
        var resourceUrl =  'api/_search/easyroommates/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
