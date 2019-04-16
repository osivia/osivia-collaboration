$JQry(function() {
	
	$JQry(".workspace-member-management").each(function(index, element) {
		var $element = $JQry(element);
		
		if (!$element.data("loaded")) {
			// Select2
			$element.find("select.select2.select2-invitation").each(function(index, element) {
				var $element = $JQry(element);
				var url = $element.data("url");
				var minimumInputLength = $element.data("minimum-input-length");
				var ajaxDataFunction = $element.data("ajax-data-function");
	 			var options = {
					closeOnSelect: true,
					minimumInputLength: (minimumInputLength ? minimumInputLength : 3),
					theme: "bootstrap"
				};
			
				// Ajax
				options["ajax"] = {
					url: url,
					dataType: "json",
					delay: 1000,
					data: function(params) {
						var result;
						
						if (ajaxDataFunction) {
							result = window[ajaxDataFunction]($element, params);
						} else {
							result = {
								filter: params.term,
								page: params.page
							};
						}
						
						return result;
					},
					processResults: function(data, params) {
						params.page = params.page || 1;
						
						return {
							results: data.items,
							pagination: {
								more: (params.page * data.pageSize) < data.total
							}
						};
					},
					cache: true
				};
				
				// Tokenizer
				options["tokenizer"] = function(input, selection, callback) {
					var term = input.term;
	
					if ((term.indexOf(",") !== -1) || (term.indexOf(";") !== -1)) {
						if (term.length > 3) {
							$JQry.getJSON(url, {
								filter: term,
								tokenizer: true
							}, function(data, status, xhr) {
								$JQry.each(data.items, function(key, value) {
									callback(value);
								});
							});
						}
						
						return {
							term: ""
						};
					} else {
						return input;
					}
				};
				
				// Result template
				options["templateResult"] = function(params) {
					$result = $JQry(document.createElement("div"));
					
					if (params.loading) {
						$result.text(params.text);
					} else if (params.message) {
						$result.addClass("text-muted");
						$result.text(params.message);
					} else {
						$result.addClass("person");
						
						// Person avatar
						$personAvatar = $JQry(document.createElement("div"));
						$personAvatar.addClass("person-avatar");
						$personAvatar.appendTo($result);
						
						if (params.create) {
							// Icon
							$icon = $JQry(document.createElement("i"));
							$icon.addClass("glyphicons glyphicons-user-add");
							$icon.text("");
							$icon.appendTo($personAvatar);
						} else if (params.avatar) {
							// Avatar
							$avatar = $JQry(document.createElement("img"));
							$avatar.attr("src", params.avatar);
							$avatar.attr("alt", "");
							$avatar.appendTo($personAvatar);
						}
						
						// Person title
						$personTitle = $JQry(document.createElement("div"));
						$personTitle.addClass("person-title");
						$personTitle.text(params.displayName);
						$personTitle.appendTo($result);
						
						// Person extra
						if (params.extra) {
							$personExtra = $JQry(document.createElement("div"));
							$personExtra.addClass("person-extra");
							$personExtra.text(params.extra);
							$personExtra.appendTo($result);
						}
					}
	
					return $result;
				};
				
				// Selection template
				options["templateSelection"] = function(params) {
					var avatarUrl;
					
					// Selection
					$selection = $JQry(document.createElement("div"));
					$selection.addClass("person");
					
					// Avatar URL
					if (params.avatar === undefined) {
						avatarUrl = $JQry(params.element).data("avatar");
					} else {
						avatarUrl = params.avatar;
					}
					
					if (avatarUrl !== undefined) {
						// Person avatar
						$personAvatar = $JQry(document.createElement("div"));
						$personAvatar.addClass("person-avatar");
						$personAvatar.appendTo($selection);
						
						// Avatar
						$avatar = $JQry(document.createElement("img"));
						$avatar.attr("src", avatarUrl);
						$avatar.attr("alt", "");
						$avatar.appendTo($personAvatar);
					}
					
					// Person title
					$personTitle = $JQry(document.createElement("div"));
					$personTitle.addClass("person-title");
					if (params.create) {
						$personTitle.text(params.id);
					} else if (params.displayName === undefined) {
						$personTitle.text(params.text);
					} else {
						$personTitle.text(params.displayName);
					}
					$personTitle.appendTo($selection);
					
					return $selection;
				};
				
				// Internationalization
				options["language"] = {};
				if ($element.data("input-too-short") !== undefined) {
					options["language"]["inputTooShort"] = function() {
						return $element.data("input-too-short");
					}
				}
				if ($element.data("error-loading") !== undefined) {
					options["language"]["errorLoading"] = function() {
						return $element.data("error-loading");
					}
				}
				if ($element.data("loading-more") !== undefined) {
					options["language"]["loadingMore"] = function() {
						return $element.data("loading-more");
					}
				}
				if ($element.data("searching") !== undefined) {
					options["language"]["searching"] = function() {
						return $element.data("searching");
					}
				}
				if ($element.data("no-results") !== undefined) {
					options["language"]["noResults"] = function() {
						return $element.data("no-results");
					}
				}
				
				// Force width to 100%
				$element.css({
					width: "100%"
				});
				
				$element.select2(options);
				
				// Display collapsed buttons
				$element.on("select2:opening", function(event) {
					var $form = $element.closest("form"),
						$collapse = $form.find(".collapse").not(".invitations-other-options");
					
					if (!$collapse.hasClass("in")) {
						$collapse.collapse("show");
					}
				});
			});
			
			
			$element.find("select").change(function(event) {
				var $target = $JQry(event.target);
				var $row = $target.closest(".table-row");
				var $edited = $row.find("input[type=hidden][id$='.edited']");
				var $form = $target.closest("form");
				var $collapse = $form.find(".collapse").not(".invitations-other-options");
				
				$edited.val(true);
				
				if (!$collapse.hasClass("in")) {
					$collapse.collapse("show");
				}
			});
			
			
			$element.find("button[type=reset]").click(function(event) {
				var $target = $JQry(event.target);
				var $form = $target.closest("form");
				
				$form.find("fieldset[disabled]").prop("disabled", false);
				$form.find("input[type=hidden][value=true]").val(false);
				$form.find("select.select2").val(null).trigger("change");
				$form.find("button").show();
				$form.find(".accepted-message").hide();
			});
			
			
			// Invitation request user message modal
			$element.find(".modal[id$='-user-message']").on("show.bs.modal", function(event) {
				var $button = $JQry(event.relatedTarget);
				var title = $button.data("title");
				var content = $button.data("content");
				var $modal = $JQry(this);
				
				$modal.find(".modal-title").text(title);
				$modal.find(".modal-body p").text(content);
			});

			
			$element.data("loaded", true);
		}
	});
	
});
