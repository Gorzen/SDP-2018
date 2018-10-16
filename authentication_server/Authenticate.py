import os
from flask import Flask, request, redirect
from urllib.parse import urlencode, unquote

app = Flask(__name__)

authParams = {}
authParams["client_id"] = os.environ.get("CLIENT_ID");
authParams["client_secret"] = os.environ.get("CLIENT_SECRET");
authParams["redirect_uri"] = os.environ.get("REDIRECT_URI");
authParams["scope"] = os.environ.get("SCOPE");

@app.route("/getCode")
def getCode():
	specificParams = {
		"response_type": "code"
	}
	finalParams = {
		**specificParams,
		**authParams
	}

	return redirect(
		"https://tequila.epfl.ch/cgi-bin/OAuth2IdP/auth?" + 
		unquote(urlencode(finalParams))
	)

@app.route("/getToken")
def getToken():
	specificParams = {
		"grant_type": "authorization_code"
	}
	specificParams["code"] = request.args.get("code")
	finalParams = {
		**specificParams,
		**authParams
	}

	return redirect(
		"https://tequila.epfl.ch/cgi-bin/OAuth2IdP/token?" + 
		unquote(urlencode(finalParams))
	)

if __name__ == "__main__":
    app.run()