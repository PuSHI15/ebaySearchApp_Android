<?php
	if(!empty($_GET)){
		$keyword = urlencode($_GET["keywords"]);
		$minPrice = $_GET["minPrice"];
		$maxPrice = $_GET["maxPrice"];
		$handleTime = $_GET["handle"];
		$sort = $_GET["sort"];
		$result = $_GET["result"];
		
		$requestURL = "http://svcs.ebay.com/services/search/FindingService/v1?siteid=0&SECURITY-APPNAME=USC3ea5db-108b-4d5b-a61c-9a9997ff7b4&OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&RESPONSE-DATA-FORMAT=XML";
		
		$requestURL .=("&keywords=".$keyword);
		$requestURL .=("&sortOrder=".$sort);
		$requestURL .=("&paginationInput.entriesPerPage=".$result);
		
		$itemFilters = null;
		$filters = 0;
		if(!empty($_GET["condition"])){
			$conditions = $_GET["condition"];
			$itemFilters = "itemFilter[".$filters."].name=Condition";
			$num = count($conditions);
			if($num==1){
				$itemFilters .= ("&itemFilter[".$filters."].value=".$conditions[0]);
			}else{
				for($i=0;$i<$num;$i++){
					$itemFilters .= ("&itemFilter[".$filters."].value[".$i."]=".$conditions[$i]);
				}
			}
			$filters++;
		}
		if(!empty($_GET["formats"])){
			$formats = $_GET["formats"];
			if($filters == 0){
				$itemFilters = "itemFilter[".$filters."].name=ListingType";
			}else{
				$itemFilters .= ("&itemFilter[".$filters."].name=ListingType");
			}
			$num = count($formats);
			if($num==1){
				if($formats[0]=="FixedPrice"){
					$itemFilters .= ("&itemFilter[".$filters."].value[0]=".$formats[0]);
					$itemFilters .= ("&itemFilter[".$filters."].value[1]=StoreInventory");
				}else{
					$itemFilters .= ("&itemFilter[".$filters."].value=".$formats[0]);
				}
			}else{
				$fix = 0;
				for($i=0;$i<$num;$i++){
					$itemFilters .= ("&itemFilter[".$filters."].value[".$i+$fix."]=".$formats[$i]);
					if($formats[$i]=="FixedPrice"){
						$itemFilters .= ("&itemFilter[".$filters."].value[".($i+1)."]=StoreInventory");
						$fix = 1;
					}
				}
			}
			$filters++;
		}
		if(isset($_GET["seller"])&&$_GET["seller"]=="return"){
			if($filters == 0){
				$itemFilters = "itemFilter[".$filters."].name=ReturnsAcceptedOnly&itemFilter[".$filters."].value=true";
			}else{
				$itemFilters .= ("&itemFilter[".$filters."].name=ReturnsAcceptedOnly&itemFilter[".$filters."].value=true");
			}
			$filters++;
		}
		if(!empty($_GET["ship"])){
			$ship = $_GET["ship"];
			if($filters == 0){
				$num = count($ship);
				if($num==1){
					if($ship[0]=="free"){
						$itemFilters = "itemFilter[".$filters."].name=FreeShippingOnly&itemFilter[".$filters."].value=true";
					}else{
						$itemFilters = "itemFilter[".$filters."].name=ExpeditedShippingOnly&itemFilter[".$filters."].value=Expedited";
					}
			}else{
				$itemFilters = "&itemFilter[".$filters."].name=FreeShippingOnly&itemFilter[".$filters."].value=true";
				$filters++;
				$itemFilters .= ("&itemFilter[".$filters."].name=ExpeditedShippingOnly&itemFilter[".$filters."].value=Expedited");
				}
			}else{
				$num = count($ship);
				if($num==1){
					if($ship[0]=="free"){
						$itemFilters .= ("&itemFilter[".$filters."].name=FreeShippingOnly&itemFilter[".$filters."].value=true");
					}else{
						$itemFilters .= ("&itemFilter[".$filters."].name=ExpeditedShippingOnly&itemFilter[".$filters."].value=Expedited");
					}
			}else{
				$itemFilters .= ("&itemFilter[".$filters."].name=FreeShippingOnly&itemFilter[".$filters."].value=true");
				$filters++;
				$itemFilters .= ("&itemFilter[".$filters."].name=ExpeditedShippingOnly&itemFilter[".$filters."].value=Expedited");
			}
		}
			$filters++;
		}
		if($minPrice!=null){
			if($filters == 0){
				$itemFilters = "itemFilter[".$filters."].name=MinPrice&itemFilter[".$filters."].value=".$minPrice;
			}else{
				$itemFilters .= ("&itemFilter[".$filters."].name=MinPrice&itemFilter[".$filters."].value=".$minPrice);
			}
			$filters++;
		}
		if($maxPrice!=null){
			if($filters == 0){
				$itemFilters = "itemFilter[".$filters."].name=MaxPrice&itemFilter[".$filters."].value=".$maxPrice;
			}else{
				$itemFilters .= ("&itemFilter[".$filters."].name=MaxPrice&itemFilter[".$filters."].value=".$maxPrice);
			}
			$filters++;
		}
		if($handleTime!=null){
			if($filters == 0){
				$itemFilters = "itemFilter[".$filters."].name=MaxHandlingTime&itemFilter[".$filters."].value=".$handleTime;
			}else{
				$itemFilters .= ("&itemFilter[".$filters."].name=MaxHandlingTime&itemFilter[".$filters."].value=".$handleTime);
			}
			$filters++;
		}
		
		
		if($itemFilters!=null){
			$requestURL .=("&".$itemFilters);
		}
		$requestURL .="&outputSelector[0]=SellerInfo&outputSelector[1]=PictureURLSuperSize&outputSelector[2]=StoreInfo";

		$resultXML = simplexml_load_file($requestURL);
		$resultArray = array();
		if(($resultXML->paginationOutput->totalEntries) == 0){
			$resultArray["ack"]="No results found";
		}else{
			$resultArray["ack"]=(string)$resultXML->ack;
			$resultArray["resultCount"]=(string)$resultXML->paginationOutput->totalEntries;
			$resultArray["pageNumber"]=(string)$resultXML->paginationOutput->pageNumber;
			$resultArray["itemCount"]=(string)$resultXML->paginationOutput->entriesPerPage;
			$count = 0;
			foreach ($resultXML->searchResult->children() as $item) {
				$basicArray=array();
				$basicArray["title"]=(string)$item->title;
				$basicArray["viewItemURL"]=(string)$item->viewItemURL;
				$basicArray["galleryURL"]=(string)$item->galleryURL;
				$basicArray["pictureURLSuperSize"]=(string)$item->pictureURLSuperSize;
				$basicArray["convertedCurrentPrice"]=(string)$item->sellingStatus->convertedCurrentPrice;
				$basicArray["shippingServiceCost"]=(string)$item->shippingInfo->shippingServiceCost;
				$basicArray["conditionDisplayName"]=(string)$item->condition->conditionDisplayName;
				$basicArray["listingType"]=(string)$item->listingInfo->listingType;
				$basicArray["location"]=(string)$item->location;
				$basicArray["categoryName"]=(string)$item->primaryCategory->categoryName;
				$basicArray["topRatedListing"]=(string)$item->topRatedListing;
				$sellerArray=array();
				$sellerArray["sellerUserName"]=(string)$item->sellerInfo->sellerUserName;
				$sellerArray["feedbackScore"]=(string)$item->sellerInfo->feedbackScore;
				$sellerArray["positiveFeedbackPercent"]=(string)$item->sellerInfo->positiveFeedbackPercent;
				$sellerArray["feedbackRatingStar"]=(string)$item->sellerInfo->feedbackRatingStar;
				$sellerArray["topRatedSeller"]=(string)$item->sellerInfo->topRatedSeller;
				$sellerArray["sellerStoreName"]=(string)$item->storeInfo->storeName;
				$sellerArray["sellerStoreURL"]=(string)$item->storeInfo->storeURL;
				$shippingArray=array();
				$shippingArray["shippingType"]=(string)$item->shippingInfo->shippingType;
				$shipLocation="";
				foreach ($item->shippingInfo->shipToLocations as $ship) {
					$shipLocation .= ($ship.",");
				}
				$shipToLocations = substr($shipLocation, 0,-1);
				$shippingArray["shipToLocations"]=$shipToLocations;
				$shippingArray["expeditedShipping"]=(string)$item->shippingInfo->expeditedShipping;
				$shippingArray["oneDayShippingAvailable"]=(string)$item->shippingInfo->oneDayShippingAvailable;
				$shippingArray["returnsAccepted"]=(string)$item->returnsAccepted;
				$shippingArray["handlingTime"]=(string)$item->shippingInfo->handlingTime;
				$itemArray = array();
				$itemArray["basicInfo"]=$basicArray;
				$itemArray["sellerInfo"]=$sellerArray;
				$itemArray["shippingInfo"]=$shippingArray;

				$resultArray["item".$count]=$itemArray;
				$count++;


			}
		}
		$resultJSON = json_encode($resultArray);
		echo $resultJSON;

	}
		?>