package com.gwolf.tvlauncher.ui.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gwolf.tvlauncher.data.AppModel

class AppActionBottomSheet : BottomSheetDialogFragment() {

    private var appModel: AppModel? = null
    var onActionCompleted: (() -> Unit)? = null
    var onPinToggle: ((AppModel) -> Unit)? = null
    var onHideApp: ((AppModel) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = resources.getIdentifier("dialog_app_actions", "layout", requireContext().packageName)
        return if (layoutId != 0) {
            inflater.inflate(layoutId, container, false)
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app = appModel ?: return
        val pkg = requireContext().packageName

        val tvTitleId = resources.getIdentifier("tv_dialog_title", "id", pkg)
        val btnPinId = resources.getIdentifier("btn_pin", "id", pkg)
        val btnHideId = resources.getIdentifier("btn_hide", "id", pkg)
        val btnAppInfoId = resources.getIdentifier("btn_app_info", "id", pkg)
        val btnUninstallId = resources.getIdentifier("btn_uninstall", "id", pkg)

        val tvTitle: TextView? = if (tvTitleId != 0) view.findViewById(tvTitleId) else null
        val btnPin: Button? = if (btnPinId != 0) view.findViewById(btnPinId) else null
        val btnHide: Button? = if (btnHideId != 0) view.findViewById(btnHideId) else null
        val btnAppInfo: Button? = if (btnAppInfoId != 0) view.findViewById(btnAppInfoId) else null
        val btnUninstall: Button? = if (btnUninstallId != 0) view.findViewById(btnUninstallId) else null

        tvTitle?.text = app.label

        btnPin?.apply {
            text = if (app.isFavorite) "Unpin" else "Pin"
            setOnClickListener {
                onPinToggle?.invoke(app)
                dismiss()
                onActionCompleted?.invoke()
            }
        }

        btnHide?.setOnClickListener {
            onHideApp?.invoke(app)
            dismiss()
            onActionCompleted?.invoke()
        }

        btnAppInfo?.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", app.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            dismiss()
        }

        btnUninstall?.setOnClickListener {
            val intent = Intent(Intent.ACTION_DELETE).apply {
                data = Uri.fromParts("package", app.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            dismiss()
            onActionCompleted?.invoke()
        }
    }

    companion object {
        fun newInstance(
            app: AppModel,
            onPinToggle: (AppModel) -> Unit,
            onHideApp: (AppModel) -> Unit,
            onActionCompleted: () -> Unit
        ): AppActionBottomSheet {
            return AppActionBottomSheet().apply {
                this.appModel = app
                this.onPinToggle = onPinToggle
                this.onHideApp = onHideApp
                this.onActionCompleted = onActionCompleted
            }
        }
    }
}
