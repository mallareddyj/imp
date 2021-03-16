
#! usr/bin/python
#
# (c) Christoph Hallmann
#
# -*- coding: utf-8 -*-
import csv, sys, os
from lxml import etree
from io import StringIO, BytesIO
import html

def main():

	csvFile = 'Facebook Jobs Feed - Dedicated West 3.9.2021 - Sheet1.csv'
	xmlFile = open('FacebookJobsFeedDedicatedWest392021.xml', 'w')
	csvData = csv.reader(open(csvFile,encoding = 'utf-8'), delimiter=',')

	header = next(csvData)
	counter = 0
	root = etree.Element('jobs')
	xmlFile.write('<?xml version="1.0" encoding="UTF-8" ?> \n')

	for row in csvData:
		products = etree.SubElement(root,'job')
		for index in range(0, len(header)):
			headLine = etree.SubElement(products, header[index])
			headLine.text = html.unescape(row[index]) # row[index]
			# headLine.text = "<![CDATA[ " + headLine.text + " ]]>"
			if(header[index] == "Description"):
				headLine.text = "<![CDATA[ " + headLine.text + " ]]>"
			# print(headLine.text)
			products.append(headLine)

	result = etree.tostring(root, pretty_print=True)
	result = result.decode()
	result = html.unescape(result)
	#bprint(result)
	xmlFile.write(result)

if __name__ == '__main__':
	main()