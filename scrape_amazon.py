#!/usr/bin/env python

# A simple code to crawl a TV price from Amazon
# Along with cron deamon, this job can be done
# regularly.
# http://www.adminschoice.com/crontab-quick-reference

# urllib2 is blocked by Amazon
# http://stackoverflow.com/questions/25936072/python-urllib2-httperror-http-error-503-service-unavailable-on-valid-website
import urllib2

from bs4 import BeautifulSoup
import time
import requests

# Initilize urls to crawl
prime_cf391 = "https://www.amazon.com/Samsung-Curved-32-Inch-Monitor-C32F391/dp/B01D3BDXQA/ref=sr_1_1?ie=UTF8&qid=1491259672&sr=8-1&keywords=cf391"
nonprime_cf391 = "https://www.amazon.com/Samsung-Curved-32-Inch-Monitor-C32F391/dp/B01D3BDXQA/ref=sr_1_1?ie=UTF8&qid=1491261690&sr=8-1&keywords=cf391"
cf391 = [prime_cf391, nonprime_cf391]

outputFile = open("somefile", "a+")

for i in range(len(cf391)):
    
    headers = {'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36'}
    req = requests.get(cf391[i],headers=headers)
    soup = BeautifulSoup(req.text, "lxml")
    price = soup.find(id="priceblock_ourprice").string
    localtime = time.asctime(time.localtime(time.time()))
    if i == 0:
        outputFile.write("prime:\t\t" + localtime + "\t" + price + "\n")
    else:
        outputFile.write("nonprime:\t" + localtime + "\t" + price + "\n")
outputFile.close()
