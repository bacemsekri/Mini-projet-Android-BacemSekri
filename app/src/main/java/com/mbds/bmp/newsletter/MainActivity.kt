package com.mbds.bmp.newsletter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.mbds.bmp.newsletter.database.ArticleRoomDatabase
import com.mbds.bmp.newsletter.databinding.ActivityMainBinding
import com.mbds.bmp.newsletter.fragments.AboutUsFragment
import com.mbds.bmp.newsletter.fragments.ArticlesFragment
import com.mbds.bmp.newsletter.fragments.CategoriesFragment
import com.mbds.bmp.newsletter.services.ArticleOfflineService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var menu: Menu? = null
    var onFavoriteClick: ((Menu) -> Boolean)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(baseContext))
        changeFragment(CategoriesFragment(), false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            this.menu = menu
            menuInflater.inflate(R.menu.menu, menu)
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (menu != null) {
            if (item.itemId == R.id.aboutUs) {
                changeFragment(AboutUsFragment())
                return true
            }
            if (item.itemId == R.id.favorites) {
                return onFavoriteClick?.let { it(menu!!) } ?: run {
                    val articleOfflineService =
                        ArticleOfflineService(ArticleRoomDatabase.getDatabase(this).articleDao())
                    changeFragment(ArticlesFragment.newInstance(articleOfflineService))
                    return true
                }
            }
        }
        return false
    }

    fun changeFavoriteIcon(@DrawableRes icon: Int) {
        menu?.findItem(R.id.favorites)?.setIcon(icon)
    }

    fun setMenuVisible(isVisible: Boolean) {
        menu?.forEach { it.isVisible = isVisible }
    }

    fun setFavoriteVisible(isVisible: Boolean) {
        menu?.findItem(R.id.favorites)?.isVisible = isVisible
    }

    fun changeFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            if (addToBackStack)
                addToBackStack(null)
        }.commit()
    }
}