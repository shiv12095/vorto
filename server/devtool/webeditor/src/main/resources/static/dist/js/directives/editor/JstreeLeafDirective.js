define(["../init/AppDirective"], function(directives) {
  directives.directive("jstreeLeaf", jstreeLeafDirective);

  function jstreeLeafDirective(){
    var directive = {
      restrict: "C",
      link: link
    }

    console.log("BHACK");

    return directive;

    function link(scope, elem, attrs) {
      elem.bind('dblclick', function() {
        console.log('Oh Maaki');
      });
    }
  }
});
