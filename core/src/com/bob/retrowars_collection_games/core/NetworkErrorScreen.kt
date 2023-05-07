package com.bob.retrowars_collection_games.core

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.I18NBundle
import com.bob.beatgame.ui.UI_SPACE
import com.bob.beatgame.ui.addToggleAudioButtonToMenuStage
import com.bob.beatgame.ui.makeButton
import com.bob.beatgame.ui.makeHeading
import com.bob.retrowars_collection_games.RetrowarsGame
import com.bob.retrowars_collection_games.UiAssets
import com.bob.retrowars_collection_games.net.Network
import com.bob.retrowars_collection_games.net.RetrowarsClient
import com.bob.retrowars_collection_games.net.RetrowarsServer
import com.bob.retrowars_collection_games.ui.makeContributeServerInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NetworkErrorScreen(game: RetrowarsGame, code: Int, message: String): Scene2dScreen(game, { game.showMainMenu() }) {

    init {

        // Ensure that we don't have any lingering network connections around.
        GlobalScope.launch {
            RetrowarsClient.get()?.listen({ _, _ -> })
            RetrowarsClient.disconnect()
            RetrowarsServer.stop()
        }

        val styles = game.uiAssets.getStyles()
        val strings = game.uiAssets.getStrings()

        val container = VerticalGroup().apply {
            setFillParent(true)
            align(Align.center)
            space(UI_SPACE * 3)

            addActor(
                makeHeading(strings["network-error.title"], styles, strings)
            )

            addActor(makeErrorInfo(code, message, styles, strings))

            addActor(
                makeButton(strings["btn.main-menu"], styles) {
                    game.showMainMenu()
                }
            )
        }

        stage.addActor(container)

        addToggleAudioButtonToMenuStage(game, stage)

    }

    private fun makeErrorInfo(code: Int, message: String, styles: UiAssets.Styles, strings: I18NBundle): Actor = when (code) {
        Network.ErrorCodes.NO_ROOMS_AVAILABLE -> showNoRoomsAvailable(styles, strings)
        Network.ErrorCodes.CLIENT_CLOSED_APP -> showClientClosedApp(styles, strings)
        Network.ErrorCodes.PLAYER_ID_IN_USE -> showPlayerIdInUse(styles, strings)
        Network.ErrorCodes.SERVER_SHUTDOWN -> showServerShutdown(styles, strings)
        else -> makeTitle(message, styles)
    }

    private fun makeReconnectButton(styles: UiAssets.Styles, label: String): Button? {
        val lastServer = RetrowarsClient.getLastServer() ?: return null
        return makeButton(label, styles) {
            game.showMultiplayerLobbyAndConnect(lastServer)
        }
    }

    private fun showNoRoomsAvailable(styles: UiAssets.Styles, strings: I18NBundle) = VerticalGroup().apply {
        space(UI_SPACE * 2)
        addActor(makeTitle(strings["network-error.server-full.title"], styles))
        addActor(makeContributeServerInfo(game.uiAssets))
        makeReconnectButton(styles, strings["network-error.btn.try-again"])?.also { reconnect ->
            addActor(reconnect)
        }
    }

    private fun showClientClosedApp(styles: UiAssets.Styles, strings: I18NBundle) = VerticalGroup().apply {
        space(UI_SPACE * 2)
        addActor(makeTitle(strings["network-error.remain-open.title"], styles))
        makeReconnectButton(styles, strings["network-error.btn.rejoin"])?.also { reconnect ->
            addActor(reconnect)
        }
    }

    private fun showPlayerIdInUse(styles: UiAssets.Styles, strings: I18NBundle) = VerticalGroup().apply {
        space(UI_SPACE * 2)
        addActor(makeTitle(strings["network-error.avatar-in-use.title"], styles))
        addActor(makeDetails(strings["network-error.avatar-in-use.details"], styles))
        makeReconnectButton(styles, strings["network-error.btn.try-again"])?.also { reconnect ->
            addActor(reconnect)
        }
    }

    private fun showServerShutdown(styles: UiAssets.Styles, strings: I18NBundle) = VerticalGroup().apply {
        space(UI_SPACE * 2)
        addActor(makeTitle(strings["network-error.server-shutdown.title"], styles))
        addActor(makeDetails(strings["network-error.server-shutdown.details"], styles))
    }

    private fun makeTitle(errorMessage: String, styles: UiAssets.Styles) =
        Label(errorMessage, styles.label.medium).apply {
            setAlignment(Align.center)
        }

    private fun makeDetails(details: String, styles: UiAssets.Styles) =
        Label(details, styles.label.small).apply {
            setAlignment(Align.center)
        }

}
