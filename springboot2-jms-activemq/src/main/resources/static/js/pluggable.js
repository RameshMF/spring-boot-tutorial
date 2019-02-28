var pluggable = '<div id=\"username-page\"> '
		+ '        <div class=\"username-page-container\">\n'
		+ '            <h1 class=\"title\">Type your username</h1>\n'
		+ '            <form id=\"usernameForm\" name=\"usernameForm\">\n'
		+ '                <div class=\"form-group\">\n'
		+ '                    <input type=\"text\" id=\"name\" placeholder=\"Username\" autocomplete=\"off\" class=\"form-control\" />\n'
		+ '                </div>\n'
		+ '                <div class=\"form-group\">\n'
		+ '                    <button type=\"submit\" class=\"accent username-submit\">Start Chatting</button>\n'
		+ '                </div>\n' + '            </form>\n'
		+ '        </div>\n' + '    </div>\n' + '\n'
'    <div id=\"chat-page\" class=\"hidden\">\n'
		+ '        <div class=\"chat-container\">\n'
		+ '            <div class=\"chat-header\">\n'
		+ '                <h2>Spring WebSocket Chat Demo</h2>\n'
		+ '            </div>\n' + '            <div class=\"connecting\">\n'
		+ '                Connecting...\n' + '            </div>\n'
		+ '            <ul id=\"messageArea\">\n' + '\n'
'            </ul>\n'
		+ '            <form id=\"messageForm\" name=\"messageForm\" nameForm=\"messageForm\">\n'
		+ '                <div class=\"form-group\">\n'
		+ '                    <div class=\"input-group clearfix\">\n'
		+ '                        <input type=\"text\" id=\"message\" placeholder=\"Type a message...\" autocomplete=\"off\" class=\"form-control\"/>\n'
		+ '                        <button type=\"submit\" class=\"primary\">Send</button>\n'
		+ '                    </div>\n' + '                </div>\n'
		+ '            </form>\n' + '        </div>\n' + '    </div>' +

		// console.log(stompClient);

		function connect() {
			username = document.querySelector('#name').value.trim();

			if (username) {
				usernamePage.classList.add('hidden');
				chatPage.classList.remove('hidden');

				/*var socket = new SockJS('/ws');
				stompClient = Stomp.over(socket);

				stompClient.connect({}, onConnected, onError);*/
			}
			event.preventDefault();
		}
$(document).ready(function() {
	$(".pluggable").append(pluggable)

	var usernameForm = document.getElementById('usernameForm');
	if (usernameForm) {
		usernameForm.addEventListener('submit', connect, true)
	}

	var messageForm = document.getElementById('messageForm');
	if (messageForm) {
		messageForm.addEventListener('submit', sendMessage, true)
	}
});
