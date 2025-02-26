import requests

url = 'http://apis.data.go.kr/9760000/PofelcddInfoInqireService/getPoelpcddRegistSttusInfoInqire'
params ={'serviceKey' : 'dKNTmNiGHpcJLrC4HIJc3nW4AaPPKOQLCvlzV7IQZDTTztv6PTuDusZxS8iC1vpBEtkLsnk97WEzKpEvf3Zqgg%3D%3D', 'pageNo' : '1', 'numOfRows' : '10', 'sgId' : '20240410', 'sgTypecode' : '2', 'sggName' : '', 'sdName' : '' }

response = requests.get(url, params=params)
print(response.content)