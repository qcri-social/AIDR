$(document).ready(function (){

    $.ajax({                                      
      url: BASE_URL+'/protected/home',              
      type: "post",          
                    
      success: function() {
          $('#current_page').append("loading..");
          console.log('hello');
          },
      error: function() {
setTimeout(function(){ $("#adminButton").hide(); console.log('Loaded'); }, 700);
         
         
          },
   });

    
});
function goToAdmin() {
    location.href = BASE_URL + '/protected/home';
    }