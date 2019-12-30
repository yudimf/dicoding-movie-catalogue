package id.yudimf.moviecatalogue2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.yudimf.moviecatalogue2.alarm.AlarmReceiver
import id.yudimf.moviecatalogue2.alarm.NotificationItem


class MainActivity : AppCompatActivity() {

    private lateinit var alarmReceiver: AlarmReceiver
    private var idNotification = 0
    private val stackNotif = ArrayList<NotificationItem>()


    companion object {
        private const val CHANNEL_NAME = "dicoding channel"
        private const val GROUP_KEY_EMAILS = "group_key_emails"
        private const val NOTIFICATION_REQUEST_CODE = 200
        private const val MAX_NOTIFICATION = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBottomNavigation()

        setReminder()
        val sender = "Yudi"
        val message = "Ngetest lagi boy"
        for(i in 1..9){
            sendFormattedNotification("$sender $i",message)
        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        stackNotif.clear()
        idNotification = 0
    }

    private fun sendFormattedNotification(sender : String, message : String){
        stackNotif.add(NotificationItem(idNotification, sender, message))
        sendNotif()
        idNotification++
    }

    private fun sendNotif() {
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_notifications_white)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mBuilder: NotificationCompat.Builder
        //Melakukan pengecekan jika idNotification lebih kecil dari Max Notif
        val CHANNEL_ID = "channel_01"
        if (idNotification < MAX_NOTIFICATION) {
            mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("New Email from " + stackNotif[idNotification].sender)
                .setContentText(stackNotif[idNotification].message)
                .setSmallIcon(R.drawable.ic_mail_white)
                .setLargeIcon(largeIcon)
                .setGroup(GROUP_KEY_EMAILS)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        } else {
            val inboxStyle = NotificationCompat.InboxStyle()
                .addLine("New Email from " + stackNotif[idNotification].sender)
                .addLine("New Email from " + stackNotif[idNotification - 1].sender)
                .setBigContentTitle("$idNotification new emails")
                .setSummaryText("mail@dicoding")

            mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("$idNotification new emails")
                .setContentText("mail@dicoding.com")
                .setSmallIcon(R.drawable.ic_mail_white)
                .setGroup(GROUP_KEY_EMAILS)
                .setGroupSummary(true)
                .setContentIntent(pendingIntent)
                .setStyle(inboxStyle)
                .setAutoCancel(true)
        }
        /*
        Untuk android Oreo ke atas perlu menambahkan notification channel
        Materi ini akan dibahas lebih lanjut di modul extended
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }
        val notification = mBuilder.build()
        mNotificationManager.notify(idNotification, notification)
    }

    private fun setReminder(){
        alarmReceiver = AlarmReceiver()
        val repeatTime = "01:46"
        val repeatMessage = getString(R.string.reminder)
        alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING,
            repeatTime, repeatMessage)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomNavigation(){
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_movie, R.id.navigation_tv_show, R.id.navigation_favorite
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

}
