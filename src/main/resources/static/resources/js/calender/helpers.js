angular
  .module('mwl.calendar.docs')
  .factory('alert', function($uibModal) {
	  console.log('helper js called '+$uibModal+' event ');
	  
    function show(action, event) {
    	console.log('helper js called '+action+' event '+JSON.stringify(event));
    	$('#myModal').modal('show');
      return $uibModal.open({
    	
       // templateUrl: '/resources/js/calender/modalContent.html',
    	  templateUrl: 'modalContent.html',
        controller: function() {
          var vm = this;
          vm.action = action;
          vm.event = event;
        },
        controllerAs: 'vm'
      });
    }

    return {
      show: show
    };

  });
