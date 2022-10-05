let profile;
let currentBitcoinInfo;
let ongoingAuctions;
let platformComission;
let currentAuction;
let comissions;
let authorization;

$(() => {
    switchTo("login");
    // Login Page Events---------------------------------------------------------------------------------
    $("#login_to_register").click((e) => {
        e.preventDefault();
        switchTo("register");
    });

    $("#login_form").submit(() => {
        loginActions.login();
        return false;
    });
    // Register Page Events---------------------------------------------------------------------------------
    $("#register_to_login").click((e) => {
        e.preventDefault();
        switchTo("login");
    });

    $("#register_form").submit(() => {
        registerActions.register();
        return false;
    });

    var path = window.location.href; // because the 'href' property of the DOM element is the absolute path
    $("#layoutSidenav_nav .sb-sidenav a.nav-link").each(function() {
        if (this.href === path) {
            $(this).addClass("active");
        }
    });

    // Toggle the side navigation
    $("#sidebarToggle").on("click", function(e) {
        e.preventDefault();
        $("body").toggleClass("sb-sidenav-toggled");
    });

    $("#show_hide_password a").on("click", function(event) {
        event.preventDefault();
        if ($("#show_hide_password input").attr("type") == "text") {
            $("#show_hide_password input").attr("type", "password");
            $("#eye_icon").addClass("fa-eye-slash");
            $("#eye_icon").removeClass("fa-eye");
        } else if ($("#show_hide_password input").attr("type") == "password") {
            $("#show_hide_password input").attr("type", "text");
            $("#eye_icon").removeClass("fa-eye-slash");
            $("#eye_icon").addClass("fa-eye");
        }
    });

    $("#main_logout_btn").click((e) => {
        mainEvents.onLogout(e);
    });

    $("#main_purchases_btn").click((e) => {
        mainEvents.onPurchasesBtnClick(e);
    });

    $("#main_ongoing_auctions_btn").click((e) => {
        mainEvents.onOngoingAuctionsBtnClick(e);
    });

    $("#main_your_auctions_btn").click((e) => {
        mainEvents.onYourAuctionsBtnClick(e);
    });

    $("#main_your_bids_btn").click((e) => {
        mainEvents.onYourBidsBtnClick(e);
    });

    $("#main_comissions_btn").click((e) => {
        mainEvents.onComissionsBtnClick(e);
    });

    $("#main_profile_btn").click((e) => {
        mainEvents.onProfileBtnClick(e);
    });

    let mainBitcoinsDropdown = $("#main_bicoins_dropdown");
    mainBitcoinsDropdown.on("show.bs.dropdown", () => {
        mainEvents.onShowBitcoinDropdown(mainBitcoinsDropdown);
    });

    $("#main_bitcoins_form").submit(() => {
        mainActions.buyBitcoins();
        return false;
    });

    $("#main_euros_deposit_form").submit(() => {
        mainActions.depositMoney();
        return false;
    });

    let main_bitcoins_amount_field = $("#main_bitcoins_amount_field");
    main_bitcoins_amount_field.on("input", () => {
        $("#main_bitcoins_total_price_display").val(
            main_bitcoins_amount_field.val() *
            (currentBitcoinInfo.bitcoinPrice +
                currentBitcoinInfo.bitcoinPrice * platformComission)
        );
    });

    $(".dropdown-menu").click((e) => {
        e.stopPropagation();
    });

    $("#main_create_auctions_form").submit((params) => {
        mainEvents.onCreateAuction();
        return false;
    });

    $("#main_ongoing_auctions_bid_form").submit((params) => {
        mainEvents.onBid();
        return false;
    });

    $("#modal_bid_form_bitcoins_field")
        .add("#modal_bid_form_euros_field")
        .on("input", () => {
            mainEvents.onBidFormInput();
        });

    $("#modal_bid_form").submit((params) => {
        mainEvents.onBid();
        return false;
    });
});

function switchTo(page) {
    $(".webpage").hide();
    $("#" + page + "_page").show();
}

function notify(options) {
    let title = $("#modal_notification_label");
    if (options.hasOwnProperty("title")) {
        title.show();
        title.text(options.title);
    } else title.hide();

    let body = $("#modal_notification_body");
    if (options.hasOwnProperty("message")) {
        body.show();
        body.text(options.message);
    } else body.hide();

    let modal = $("#modal_notification");
    modal.on("hidden.bs.modal", () => {
        if (options.hasOwnProperty("onClose")) options.onClose();
    });
    modal.modal();
}

function confirm(options) {
    let title = $("#modal_confirmation_label");
    if (options.hasOwnProperty("title")) {
        title.show();
        title.text(options.title);
    } else title.hide();

    let body = $("#modal_confirmation_body");
    if (options.hasOwnProperty("message")) {
        body.show();
        body.text(options.message);
    } else body.hide();

    let modal = $("#modal_confirmation");
    $("#modal_confirmation_btn").click(() => {
        if (options.hasOwnProperty("onConfirm")) options.onConfirm();
    });
    modal.modal();
}

function error(data) {
    console.log(data);
    if (data.hasOwnProperty('responseJSON')) {
        if (data.responseJSON.hasOwnProperty('violations')) {
            data.responseJSON.violations.forEach((violation) => {
                notify({
                    title: violation.title,
                    message: violation.message
                });
            });
        } else {
            notify({
                title: data.responseJSON.title,
                message: data.responseJSON.message
            });
        }
    } else {
        notify({
            title: "An unhandled server error occurred",
            message: JSON.stringify(data)
        });
    }
}

function getProfile(options) {
    $.get({
        url: `/profiles/me`,
        dataType: "json",
        contentType: "application/json",
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", authorization);
        },
        success: (data) => {
            profile = data;
            mainActions.fillProfileInfo();
            if (options.hasOwnProperty("success")) options.success(data);
        },
        error: (data) => {
            if (options.hasOwnProperty("error")) options.error(data);
            error(data);
        },
    });
}

let mainEvents = {
    onLogout: (e) => {
        e.preventDefault();
        mainActions.logout();
    },

    onPurchasesBtnClick: (e) => {
        e.preventDefault();
        mainActions.getPurchases();
        mainActions.displaySection("purchases");
    },

    onOngoingAuctionsBtnClick: (e) => {
        e.preventDefault();
        mainActions.displaySection("ongoing_auctions");
        mainActions.getAllAuctions();
    },

    onYourAuctionsBtnClick: (e) => {
        e.preventDefault();
        mainActions.getAuctions();
        mainActions.displaySection("your_auctions");
    },

    onYourBidsBtnClick: (e) => {
        e.preventDefault();
        mainActions.displaySection("your_bids");
        mainActions.getUserBids();
        mainActions.getUserBidsWon();
        mainActions.getBlockedMoney();
    },

    onComissionsBtnClick: (e) => {
        e.preventDefault();
        mainActions.displaySection("comissions");
        mainActions.getComissions();
    },

    onProfileBtnClick: (e) => {
        e.preventDefault();
        mainActions.displaySection("profile");
    },

    onShowBitcoinDropdown: (mainBitcoinsDropdown) => {
        if (profile.type === "BIDDER") {
            mainBitcoinsDropdown.dropdown("toggle");
        } else {
            mainActions.getBitcoinPrice();
        }
    },

    onCreateAuction: () => {
        mainActions.createAuction();
    },

    onBid: () => {
        let bitcoinsField = $("#modal_bid_form_bitcoins_field");
        let priceField = $("#modal_bid_form_euros_field");
        let finalPriceField = $("#modal_bid_form_price_display");
        priceField.removeClass("is-invalid");
        bitcoinsField.removeClass("is-invalid");
        finalPriceField.removeClass("is-invalid");
        let bid = {
            bitcoins: parseFloat(bitcoinsField.val()),
            euros: parseFloat(priceField.val()),
        };
        if (bid.euros < currentAuction.initialPrice) {
            priceField.addClass("is-invalid");
            $("#modal_bid_form_euros_field_invalid").html(
                `Must be equal or higher than the auction initial price: ${currentAuction.initialPrice}.`
            );
        } else if (bid.euros > profile.account.euros) {
            priceField.addClass("is-invalid");
            $("#modal_bid_form_euros_field_invalid").html(
                `You don't have enought money.`
            );
        } else if (bid.bitcoins > currentAuction.bitcoins) {
            bitcoinsField.addClass("is-invalid");
            $("#modal_bid_form_bitcoins_field_invalid").html(
                `Too much bitcoins, max: ${currentAuction.bitcoins}`
            );
        } else if (finalPriceField.val() > profile.account.euros) {
            finalPriceField.addClass("is-invalid");
            $("#modal_bid_form_price_display_invalid").html(
                `You don't have enought money.`
            );
        } else {
            mainActions.createBid(bid);
        }
    },

    onAuctionParticipate: (e) => {
        currentAuction = ongoingAuctions.find(
            (auction) => auction.id === e.target.value
        );
        $("#modal_bid_form_comission_display").text(
            `Platform comission: ${platformComission * 100}%`
        );
        $("#modal_bid").modal();
    },

    onBidFormInput: () => {
        $("#modal_bid_form_price_display").val(() => {
            let price =
                $("#modal_bid_form_bitcoins_field").val() *
                $("#modal_bid_form_euros_field").val();
            return price + price * platformComission;
        });
    },
};

let loginActions = {
    login: () => {
        $.post({
            url: "/login",
            data: JSON.stringify({
                username: $("#login_username").val(),
                password: $("#login_password").val(),
            }),
            contentType: "application/json",
            success: (data, textStatus, request) => {
                authorization = request.getResponseHeader("authorization");
                $.get({
                    url: "/platform-comission",
                    success: (param) => {
                        console.log(param);
                        platformComission = param;
                    },
                });
                getProfile({
                    success: (data) => {
                        loginActions.loginSuccess(data);
                    },
                    error: (data) => {
                        loginActions.loginFail(data);
                    },
                });
            },
            error: (data) => {
                loginActions.loginFail(data);
            },
        });
    },

    loginSuccess: (data) => {
        $("#login_form").trigger("reset");
        notify({
            title: "Logged in successfully!",
            message: `Welcome back ${profile.userName}.`,
            onClose: () => {
                switchTo("main");
                mainActions.displaySection("profile");
                mainActions.fillProfileInfo();
            },
        });
    },

    loginFail: (data) => {
        $("#login_username").removeClass("is-valid");
        $("#login_password").removeClass("is-valid");
        $("#login_username").addClass("is-invalid");
        $("#login_password").addClass("is-invalid");
    },
};

let registerActions = {
    register: () => {
        let password1 = $("#register_password").val();
        let password2 = $("#register_password_confirm").val();

        if (password1 !== password2) {
            registerActions.passwordConfirmFail();
            return;
        }

        let profile = {
            userName: $("#register_username").val(),
            password: password2,
            email: $("#register_email").val(),
            type: $("#register_type").find(":selected").val(),
        };

        $.post({
            url: `/profiles`,
            data: JSON.stringify(profile),
            contentType: "application/json",
            dataType: "json",
            success: (data) => {
                registerActions.registerSuccess(data);
            },
            error: (data) => {
                error(data);
            },
        });
    },

    passwordConfirmFail: () => {
        $("#register_password").removeClass("is-valid");
        $("#register_password_confirm").removeClass("is-valid");
        $("#register_password").addClass("is-invalid");
        $("#register_password_confirm").addClass("is-invalid");
    },

    registerSuccess: (data) => {
        $("#register_form").trigger("reset");
        notify({
            title: "Registered successfully!",
            message: `Your profile ${data.userName} has been created.`,
            onClose: () => {
                location.reload();
            },
        });
    },
};

let mainActions = {
    fillProfileInfo: () => {
        $("#main_username_display").text(profile.userName);
        $("#main_username_field").val(profile.userName);
        $("#main_email_field").val(profile.email);
        $("#main_password_field").val(profile.password);
        $("#main_type_field").val(profile.type);
        $("#main_bitcoins_field").text(
            Number(profile.account.bitcoins).toLocaleString("es")
        );
        $("#main_euros_display").text(
            Number(profile.account.euros).toLocaleString("es")
        );
        $(".restricted-option").hide();
        $(`.${profile.type.toLowerCase()}-access`).show();
    },

    displaySection: (section) => {
        $(".profile-section").hide();
        $("#main_" + section + "_section").show();
    },

    logout: () => {
        confirm({
            title: "Logout",
            message: `Are you sure to logout from ${profile.userName}?`,
            onConfirm: () => {
                location.reload();
            },
        });
    },

    getBitcoinPrice: () => {
        $.get({
            url: `/bitcoins/price`,
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", authorization);
            },
            success: (data) => {
                currentBitcoinInfo = data;
                console.log(data);
                $("#main_bitcoins_price_display").text(
                    `1 BTC = ${currentBitcoinInfo.bitcoinPrice} EUR`
                );
                $("#main_bitcoins_comission_display").text(
                    `Platform comission: ${platformComission * 100}%`
                );
            },
            error: (data) => {
                error(data);
            },
        });
    },

    buyBitcoins: () => {
        let totalPrice = $("#main_bitcoins_total_price_display").val();
        let bitcoinAmount = $("#main_bitcoins_amount_field").val();

        if (totalPrice > profile.account.euros) {
            $("#main_bitcoins_amount_field").addClass("is-invalid");
        } else {
            $.post({
                url: `/profiles/${profile.userName}/bitcoins/${bitcoinAmount}`,
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("Authorization", authorization);
                },
                success: (data) => {
                    notify({
                        title: `Purchase successful!`,
                        message: `Your purchased ${bitcoinAmount} bitcoins for ${Number(
                            totalPrice
                        ).toLocaleString("de-DE", {
                            style: "currency",
                            currency: "EUR",
                        })} euros`,
                        onClose: (params) => {
                            mainActions.buyBitcoinsSuccess(data);
                        },
                    });
                },
                error: (data) => {
                    error(data);
                },
            });
        }
    },

    buyBitcoinsSuccess: (data) => {
        $("#main_bitcoins_amount_field").removeClass("is-invalid");
        $("#main_bitcoins_form").trigger("reset");
        getProfile({
            username: profile.userName,
            password: profile.password,
        });
        mainActions.getPurchases();
    },

    getPurchases: () => {
        $.get({
            url: `/profiles/${profile.userName}/purchases`,
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", authorization);
            },
            success: (data) => {
                profile.purchases = data;
                mainActions.fillPurchaseInfo();
            },
            error: (data) => {
                error(data);
            },
        });
    },

    fillPurchaseInfo: () => {
        let newTable = $("<tbody>");
        profile.purchases.forEach((purchase) => {
            $("<tr>")
                .append($("<td>").html(purchase.date))
                .append($("<td>").html(purchase.numBitcoins))
                .append(
                    $("<td>").html(
                        Number(
                            purchase.numEuros / purchase.numBitcoins
                        ).toLocaleString("de-DE", {
                            style: "currency",
                            currency: "EUR",
                        })
                    )
                )
                .append(
                    $("<td>").html(
                        Number(purchase.numEuros).toLocaleString("de-DE", {
                            style: "currency",
                            currency: "EUR",
                        })
                    )
                )
                .append(
                    $("<td>").html(
                        Number(purchase.comission).toLocaleString("de-DE", {
                            style: "currency",
                            currency: "EUR",
                        })
                    )
                )
                .append(
                    $("<td>").html(
                        Number(
                            purchase.numEuros + purchase.comission
                        ).toLocaleString("de-DE", {
                            style: "currency",
                            currency: "EUR",
                        })
                    )
                )
                .appendTo(newTable);
        });
        $("#main_purchases_table").find("tbody").replaceWith(newTable);
    },

    getAuctions: () => {
        $.get({
            url: `/profiles/${profile.userName}/auctions`,
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", authorization);
            },
            success: (data) => {
                profile.auctions = data;
                mainActions.getAuctionsBids(profile.auctions);
                mainActions.fillAuctionInfo();
            },
            error: (data) => {
                error(data);
            },
        });
    },

    getAuctionsBids: (auctions) => {
        auctions.forEach((auction) => {
            $.get({
                url: `/auctions/${auction.id}/bids`,
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("Authorization", authorization);
                },
                success: (data) => {
                    auction.bids = data;
                },
                error: (data) => {
                    error(data);
                },
            });
        });
    },

    fillAuctionInfo: () => {
        let newTable = $("<tbody>");
        profile.auctions.forEach((auction) => {
            $("<tr>")
                .append($("<td>").html(auction.startingDate))
                .append($("<td>").html(auction.endingDate))
                .append($("<td>").html(auction.bitcoins))
                .append(
                    $("<td>").html(
                        Number(auction.initialPrice).toLocaleString("de-DE", {
                            style: "currency",
                            currency: "EUR",
                        })
                    )
                )
                .appendTo(newTable);
        });
        $("#main_your_finished_auctions_table")
            .find("tbody")
            .replaceWith(newTable);
    },

    depositMoney: () => {
        let money = $("#main_euros_deposit_field").val();
        $.post({
            url: `/profiles/${profile.userName}/euros/${money}`,
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", authorization);
            },
            success: (data) => {
                notify({
                    title: `Deposit successful!`,
                    message: `Your deposited ${Number(money).toLocaleString(
                        "de-DE",
                        {
                            style: "currency",
                            currency: "EUR",
                        }
                    )} in your account.`,
                });
                $("#main_euros_deposit_form").trigger("reset");
                getProfile({
                    username: profile.userName,
                    password: profile.password,
                });
            },
            error: (data) => {
                error(data);
            },
        });
    },

    createAuction: () => {
        $.post({
            url: `/profiles/${profile.userName}/auctions`,
            data: JSON.stringify({
                bitcoins: $("#main_create_auction_bitcoins_field").val(),
                startingDate: $("#main_create_auction_start_date_field").val(),
                endingDate: $("#main_create_auction_finish_date_field").val(),
                initialPrice: $("#main_create_auction_price_field").val(),
                bids: [],
            }),
            contentType: "application/json",
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", authorization);
            },
            success: (data) => {
                notify({
                    title: "Auction created succesfully!",
                    message: "Your auction has been created succesfully.",
                    onClose: () => {
                        mainActions.getAuctions();
                    },
                });
            },
            error: (data) => {
                error(data);
            },
        });
    },

    createBid: (bid) => {
        $("#modal_bid").modal("hide");
        $.post({
            url: `/profiles/${profile.userName}/auctions/${currentAuction.id}/bids`,
            data: JSON.stringify(bid),
            contentType: "application/json",
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", authorization);
            },
            success: (data) => {
                notify({
                    title: "Your bid was placed!",
                    message: `You placed a bid of ${Number(
                        $("#modal_bid_form_price_display").val()
                    ).toLocaleString("de-DE", {
                        style: "currency",
                        currency: "EUR",
                    })} for ${bid.bitcoins} bitcoins.`,
                    onClose: () => {
                        getProfile({
                            username: profile.userName,
                            password: profile.password,
                        });
                    },
                });
            },
            error: (data) => {
                error(data);
            },
        });
    },

    getAllAuctions: () => {
        $.get({
            url: `/auctions`,
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", authorization);
            },
            success: (data) => {
                ongoingAuctions = data;
                mainActions.getAuctionsBids(ongoingAuctions);
                mainActions.fillAllAuctionsInfo();
            },
            error: (data) => {
                error(data);
            },
        });
    },

    fillAllAuctionsInfo: () => {
        let newTable = $("<tbody>");
        ongoingAuctions.forEach((auction) => {
            $("<tr>")
                .append($("<td>").html(auction.startingDate))
                .append($("<td>").html(auction.endingDate))
                .append($("<td>").html(auction.bitcoins))
                .append(
                    $("<td>")
                    .html(
                        Number(auction.initialPrice).toLocaleString(
                            "de-DE", {
                                style: "currency",
                                currency: "EUR",
                            }
                        )
                    )
                    .append(
                        $("<button>")
                        .addClass("btn btn-primary float-right")
                        .val(auction.id)
                        .html("Participate")
                        .click((e) => {
                            mainEvents.onAuctionParticipate(e);
                        })
                    )
                )
                .appendTo(newTable);
        });
        $("#main_ongoing_auctions_table").find("tbody").replaceWith(newTable);
    },

    getUserBids: () => {
        $.get({
            url: `/profiles/${profile.userName}/bids`,
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", authorization);
            },
            success: (data) => {
                profile.bids = data;
                mainActions.fillUserBidsInfo();
            },
            error: (data) => {
                error(data);
            },
        });
    },

    getUserBidsWon: () => {
        $.get({
            url: `/profiles/${profile.userName}/bids/won`,
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", authorization);
            },
            success: (data) => {
                profile.bidsWon = data;
                mainActions.fillUserBidsWonInfo();
            },
            error: (data) => {
                error(data);
            },
        });
    },

    fillUserBidsInfo: () => {
        let newTable = $("<tbody>");
        profile.bids.forEach((bid) => {
            $("<tr>")
                .append($("<td>").html(bid.dateP))
                .append($("<td>").html(bid.numberBitcoins))
                .append(
                    $("<td>").html(
                        Number(bid.priceEuros).toLocaleString("de-DE", {
                            style: "currency",
                            currency: "EUR",
                        })
                    )
                )
                .append($("<td>").html(bid.bitcoinsEarned))
                .append($("<td>").html(bid.comission))
                .appendTo(newTable);
        });
        $("#main_your_bids_table").find("tbody").replaceWith(newTable);
    },

    fillUserBidsWonInfo: () => {
        let newTable = $("<tbody>");
        profile.bidsWon.forEach((bid) => {
            $("<tr>")
                .append($("<td>").html(bid.dateP))
                .append($("<td>").html(bid.numberBitcoins))
                .append(
                    $("<td>").html(
                        Number(bid.priceEuros).toLocaleString("de-DE", {
                            style: "currency",
                            currency: "EUR",
                        })
                    )
                )
                .append($("<td>").html(bid.bitcoinsEarned))
                .append($("<td>").html(bid.comission))
                .appendTo(newTable);
        });
        $("#main_your_bids_won_table").find("tbody").replaceWith(newTable);
    },

    getBlockedMoney: () => {
        $.get({
            url: `/profiles/${profile.userName}/euros/blocked`,
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", authorization);
            },
            success: (data) => {
                console.log(data);
                $("#main_your_bids_money_blocked_display").text(
                    `Money blocked: ${Number(data).toLocaleString("de-DE")}`
                );
            },
            error: (data) => {
                error(data);
            },
        });
    },

    getComissions: () => {
        $.get({
            url: `/comissions`,
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", authorization);
            },
            success: (data) => {
                comissions = data;
                mainActions.fillComissionInfo();
            },
            error: (data) => {
                error(data);
            },
        });
    },

    fillComissionInfo: () => {
        let newTable = $("<tbody>");
        let total = 0;
        comissions.forEach((comission) => {
            total += comission.comission;
            $("<tr>")
                .append($("<td>").html(comission.type))
                .append($("<td>").html(comission.dateP))
                .append(
                    $("<td>").html(
                        Number(comission.euros).toLocaleString("de-DE", {
                            style: "currency",
                            currency: "EUR",
                        })
                    )
                )
                .append(
                    $("<td>").html(
                        Number(comission.comission).toLocaleString("de-DE", {
                            style: "currency",
                            currency: "EUR",
                        })
                    )
                )
                .appendTo(newTable);
        });
        $("#main_comissions_table").find("tbody").replaceWith(newTable);
        $("#main_comission_total_display").text(
            `Total euros earned: ${Number(total).toLocaleString("de-DE")}`
        );
    },
};